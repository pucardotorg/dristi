const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
// const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = {
  // mode: 'development',
  entry: "./src/index.js",
  devtool: "none",
  module: {
    rules: [
      {
        test: /\.(js)$/,
        use: ["babel-loader"],
      },
      {
        test: /\.css$/i,
        use: ["style-loader", "css-loader"],
      }
    ],
  },
  output: {
    filename: "[name].bundle.js",
    path: path.resolve(__dirname, "build"),
    publicPath: "/pucar-ui/",
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
      minSize:20000,
      maxSize:50000,
      enforceSizeThreshold:50000,
      minChunks:1,
      maxAsyncRequests:30,
      maxInitialRequests:30
    },
  },
  plugins: [
    new CleanWebpackPlugin(),
    // new BundleAnalyzerPlugin(),
    new HtmlWebpackPlugin({ inject: true, template: "public/index.html" }),
  ],
};