class lighttpd::params {
  $httpd_user  = 'root'
  $httpd_group = 'root'

  # Change to 'absent' if you want to remove lighttpd
  $ensure_package = present
}
