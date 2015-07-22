module.exports = function(grunt) {
	require('load-grunt-tasks')(grunt); // npm install --save-dev load-grunt-tasks 
	 
	grunt.initConfig({
		babel: {
			options: {
				sourceMap: true
			},
			dist: {
				files: {
					'target/mythic-draft/resources/js/app.js': 'src/main/webapp/resources/js/mythicdraft.js'
				}
			}
		}
	});
	 
	grunt.registerTask('default', ['babel']);
}