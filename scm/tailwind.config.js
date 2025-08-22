// tailwind.config.js
export default {
  content: [
    "./src/main/resources/templates/**/*.html", // Thymeleaf templates
    "./src/main/resources/static/js/**/*.js"    // Any custom JS
  ],
  theme: {
    extend: {},
  },
  darkMode: "class", // Enables class-based dark mode
  plugins: [],
};
