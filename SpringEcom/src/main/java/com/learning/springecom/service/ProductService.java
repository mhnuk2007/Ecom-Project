package com.learning.springecom.service;

import com.learning.springecom.model.Product;
import com.learning.springecom.repo.ProductRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private AiImageGenService aiImageGenService;

    @Autowired
    private VectorStore vectorStore;

    public List<Product> getAllProducts() {
        return productRepo.findAll();

    }

    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(new Product(-1));

    }

    @Transactional
    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        // Your existing image handling code...
        if (image != null && !image.isEmpty()) {
            product.setImageName(image.getOriginalFilename());
            product.setImageType(image.getContentType());
            product.setImageData(image.getBytes());
        } else if (product.getId() > 0) {
            Product existingProduct = productRepo.findById(product.getId()).orElse(null);
            if (existingProduct != null) {
                product.setImageName(existingProduct.getImageName());
                product.setImageType(existingProduct.getImageType());
                product.setImageData(existingProduct.getImageData());
            }
        }

        Product savedProduct = productRepo.save(product);
        System.out.println("Product saved with ID: " + savedProduct.getId());

        // Create embedding with detailed logging
        try {
            String content = String.format(
                    """
                    Product Name: %s
                    Description: %s
                    Brand: %s
                    Price: %s
                    Category: %s
                    ReleaseDate: %s
                    Available: %s
                    Stock: %s
                    """,
                    savedProduct.getName(),
                    savedProduct.getDescription(),
                    savedProduct.getBrand(),
                    savedProduct.getPrice(),
                    savedProduct.getCategory(),
                    savedProduct.getReleaseDate(),
                    savedProduct.isProductAvailable(),
                    savedProduct.getStockQuantity()
            );

            System.out.println("Creating document with content: " + content);

            Document document = new Document(
                    UUID.randomUUID().toString(),
                    content,
                    Map.of("productId", String.valueOf(savedProduct.getId()))
            );

            System.out.println("Adding document to vector store: " + document.getId());
            vectorStore.add(List.of(document));
            System.out.println("Successfully added document to vector store");

        } catch (Exception e) {
            System.err.println("Failed to add document to vector store: " + e.getMessage());
            e.printStackTrace();
            // Don't fail the entire operation for vector store issues
        }

        return savedProduct;
    }

    public void deleteProduct(int id) {
        productRepo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }

    public String generateDescription(String name, String category) {
        String descPrompt = String.format("""
                Write a concise and profession product description for an e-commerce listing.
                
                Product Name: %s
                Category: %s
                
                Keep it simple, engaging and higlight its primary features or benefits.
                Avoid technical jorgon and keep it customer-friendly.
                Limit the description to 250 characters maximum.
                """, name, category);

        String desc = chatClient.prompt(descPrompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();

        return desc;
    }


    public byte[] generateImage(String name, String category, String description) {
        String imagePrompt = String.format("""
                Generate a highly realistic, professional grade, e-commerce product image.
                
                Product Details:
                Category: %s
                Name: %s
                Description: %s
                
                Requirements:
                     - Use a clean, minimalistic, white or very light grey background.
                     - Ensure the product is well-lit with soft, natural-looking lighting.
                     - Add realistic shadows and soft reflections to ground the product naturally.
                     - No humans, brand logos, watermarks, or text overlays should be visible.
                     - Showcase the product from its most flattering angle that highlights key features.
                     - Ensure the product occupies a prominent position in the frame, centered or slightly off-centered.
                     - Maintain a high resolution and sharpness, ensuring all textures, colors, and details are clear.
                     - Follow the typical visual style of top e-commerce websites like Amazon, Flipkart, or Shopify.
                     - Make the product appear life-like and professionally photographed in a studio setup.
                     - The final image should look immediately ready for use on an e-commerce website without further editing.
                """, category, name, description);

        byte[] aiImage = aiImageGenService.generateImage(imagePrompt);
        return aiImage;
    }
}
