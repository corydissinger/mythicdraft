module.exports = function(grunt) {
	require('load-grunt-tasks')(grunt); // npm install --save-dev load-grunt-tasks 
	 
	grunt.initConfig({
		babel: {
			options: {
				sourceMap: false
			},
			dist: {
				files: {
					'target/mythic-draft/resources/js/mythicdraft-babeled.js': 'src/main/webapp/resources/js/mythicdraft.js',
					'target/mythic-draft/resources/js/deck-gui-babeled.js': 'src/main/webapp/resources/js/deck-gui.js',
					'target/mythic-draft/resources/js/draft-gui-babeled.js': 'src/main/webapp/resources/js/draft-gui.js',
					'target/mythic-draft/resources/js/player-gui-babeled.js': 'src/main/webapp/resources/js/player-gui.js',
					'target/mythic-draft/resources/js/stats-gui-babeled.js': 'src/main/webapp/resources/js/stats-gui.js'
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
					'target/mythic-draft/resources/js/app.js': ['target/mythic-draft/resources/js/player-gui-babeled.js',
																'target/mythic-draft/resources/js/deck-gui-babeled.js',
																'target/mythic-draft/resources/js/draft-gui-babeled.js',
																'target/mythic-draft/resources/js/stats-gui-babeled.js',																
																'target/mythic-draft/resources/js/mythicdraft-babeled.js', 
																'src/main/webapp/resources/js/bootstrap-toggle-shim.js',
																'src/main/webapp/resources/js/card-deck-organizer.js']
				}
			}
		}		
	});
	 
	grunt.registerTask('default', ['babel', 'cssmin', 'uglify']);
}