const path = require('path');

module.exports = {
	entry: "./app.js",
	output: {
		path: path.resolve(__dirname, "assets"),
		filename: "bundle.js"
	},
	module: {
		loaders: [
			{test:/\.js$/, exclude:[path.resolve(__dirname, "node_modules")], loader: 'babel-loader'}
		]
	}
};
