class lighttpd (
  $httpd_user		= $lighttpd::params::httpd_user,
  $httpd_group		= $lighttpd::params::httpd_group,
  $ensure_package	= $lighttpd::params::ensure_package,
) inherits lighttpd::params {

  package { 'lighttpd':
    ensure => $ensure_package,
  }

  if $ensure_package == present {
    file { "index.html":
      path    => "/var/www/",
      ensure  => file,
      owner   => $httpd_user,
      group   => $httpd_group,
      mode    => 0644,
      content => template('lighttpd/index.html.erb'),
    }

    file { "lighttpd.conf":
      path    => "/etc/lighttpd/",
      ensure  => file,
      owner   => $httpd_user,
      group   => $httpd_group,
      mode    => 0644,
      content => template('lighttpd/lighttpd.conf.erb'),
    }
    
    service { "lighttpd":
      ensure => running,
      enable => true,
    }

  }

}
