module.exports = function(grunt) {
	require('load-grunt-tasks')(grunt); // npm install --save-dev load-grunt-tasks 
	 
	grunt.initConfig({
		babel: {
			options: {
				sourceMap: false
			},
			dist: {
				files: {
					'target/classes/META-INF/resources/webjars/js/mythicdraft-babeled.js': 'src/main/js/mythicdraft.js',
					'target/classes/META-INF/resources/webjars/js/deck-gui-babeled.js': 'src/main/js/deck-gui.js',
					'target/classes/META-INF/resources/webjars/js/draft-gui-babeled.js': 'src/main/js/draft-gui.js',
					'target/classes/META-INF/resources/webjars/js/player-gui-babeled.js': 'src/main/js/player-gui.js',
					'target/classes/META-INF/resources/webjars/js/stats-gui-babeled.js': 'src/main/js/stats-gui.js'
				}
			}
		},
		cssmin: {
			target: {
				files: {
					'target/classes/META-INF/resources/webjars/css/app.css': 'src/main/css/mythicdraft.css'
				}
			}
		},
 	    uglify: {
			dist: {
				files: {
					'target/classes/META-INF/resources/webjars/js/app.js': ['target/classes/META-INF/resources/webjars/js/player-gui-babeled.js',
																'target/classes/META-INF/resources/webjars/js/deck-gui-babeled.js',
																'target/classes/META-INF/resources/webjars/js/draft-gui-babeled.js',
																'target/classes/META-INF/resources/webjars/js/stats-gui-babeled.js',
																'target/classes/META-INF/resources/webjars/js/mythicdraft-babeled.js',
																'src/main/js/bootstrap-toggle-shim.js',
																'src/main/js/card-deck-organizer.js']
				}
			}
		}		
	});
	 
	grunt.registerTask('default', ['babel', 'cssmin', 'uglify']);
}