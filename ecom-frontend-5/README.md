# Ecom-Frontend-5 - React Frontend for SpringEcom

A modern React-based frontend for the SpringEcom e-commerce platform, built with Vite 7+ and Axios for seamless integration with the Spring Boot backend API.
## 🚀 Features

- **Product Listing**: Browse all products with details and images
- **Product Details**: View product info, stock, and images
- **Add to Cart**: Add products to a shopping cart (with localStorage persistence)
- **Update Product**: Edit product details and images (admin only, if enabled)
- **Delete Product**: Remove products (admin only, if enabled)
- **Image Display**: Fetch and display product images from backend
- **Responsive UI**: Clean, modern, and responsive design
- **Theme Support**: Toggle between light and dark themes (with persistence)
## 🛠️ Tech Stack

- **Framework**: React 18+
- **Build Tool**: Vite 7+
- **HTTP Client**: Axios
- **Routing**: React Router DOM
- **UI Components**: React Bootstrap, Bootstrap, Bootstrap Icons
- **State Management**: React Context API
- **Styling**: CSS Modules / Plain CSS
## 📋 Prerequisites

- Node.js 18+
- npm 9+
- Backend API running (see [SpringEcom Backend](../README.md))
## ⚙️ Setup & Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ecom-frontend-5
```

### 2. Install Dependencies
```bash
npm install
```
### 3. Start the Development Server
```bash
npm run dev
```
The app will be available at `http://localhost:5173/`
## 🌐 Connecting to Backend

Ensure the backend API is running at `http://localhost:8080` (default). You can change API URLs in `src/axios.jsx` or relevant files if needed.
## 📚 API Usage Examples

The frontend interacts with these backend endpoints:

- `GET /api/products` - List all products
- `GET /api/product/{id}` - Get product details
- `GET /api/product/{id}/image` - Get product image
- `POST /api/product` - Add new product (admin only)
- `PUT /api/product/{id}` - Update product (admin only)
- `DELETE /api/product/{id}` - Delete product (admin only)
## 🗂️ Project Structure

```
ecom-frontend-5/
├── public/
│   └── vite.svg
├── src/
│   ├── App.jsx
│   ├── App.css
│   ├── main.jsx
│   ├── index.css
│   ├── axios.jsx
│   ├── assets/
│   │   ├── react.svg
│   │   └── unplugged.png
│   ├── components/
│   │   ├── AddProduct.jsx
│   │   ├── Cart.jsx
│   │   ├── CheckoutPopup.jsx
│   │   ├── Home.jsx
│   │   ├── Navbar.jsx
│   │   ├── Product.jsx
│   │   └── UpdateProduct.jsx
│   └── Context/
│       └── Context.jsx
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

## 📝 Notes

- The cart is managed with React Context and persisted in localStorage.
- Product add/update/delete features may require admin privileges (see backend for details).
- API URLs are hardcoded for local development; update as needed for deployment.
- Theme (light/dark) preference is saved and applied automatically.
## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
## 📝 License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## 📞 Contact

For questions or support, please contact:
- LinkedIn: [Mohan Lal](https://www.linkedin.com/in/mohan-lal-b79790126/)
- GitHub: [@mhnuk2007](https://github.com/mhnuk2007)
# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh
