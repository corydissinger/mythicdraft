module.exports = function(grunt) {
	require('load-grunt-tasks')(grunt); // npm install --save-dev load-grunt-tasks 
	 
	grunt.initConfig({
		babel: {
			options: {
				sourceMap: false
			},
			dist: {
				files: {
					'target/mythic-draft/resources/js/mythicdraft-babeled.js': 'src/main/webapp/resources/js/mythicdraft.js'
				}
			}
		},
		cssmin: {
			target: {
				files: {
					'target/mythic-draft/resources/css/app.css': 'src/main/webapp/resources/css/mythicdraft.css'
				}				
			}
		},
 	    uglify: {
			dist: {
				files: {
					'target/mythic-draft/resources/js/app.js': ['target/mythic-draft/resources/js/mythicdraft-babeled.js', 'src/main/webapp/resources/js/bootstrap-toggle-shim.js']
				}
			}
		}		
	});
	 
	grunt.registerTask('default', ['babel', 'cssmin', 'uglify']);
}