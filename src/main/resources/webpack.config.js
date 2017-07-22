const path = require('path');
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const styleLintPlugin = require('stylelint-webpack-plugin');

module.exports = {
	entry: "./app.js",
	output: {
		path: path.resolve(__dirname, "assets"),
		filename: "bundle.js"
	},

	plugins: [
        // Specify the resulting CSS filename
        new ExtractTextPlugin('bundle.css')
    ],

	module: {
		loaders: [
			{
                    test: /\.js$/,
                    loader: 'babel-loader',
                    exclude: /node_modules/
                  },
                  {
                    test: /\.scss$/,
                    loader: ExtractTextPlugin.extract('css-loader!sass-loader')
                  }
		]
	},
	watch: true,
    devtool: 'source-map'
};
