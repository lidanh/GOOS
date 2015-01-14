exec { 'apt-update':
  command => '/usr/bin/apt-get update',
  user    => 'root'
}

exec { 'apt-upgrade':
  command => '/usr/bin/apt-get upgrade -y',
  user    => 'root',
  require => Exec['apt-update']
}

package { "openjdk-7-jre":
    ensure  => present,
    require => Exec['apt-upgrade']
}

exec { 'wget-openfire':
  command => '/usr/bin/wget -O openfire.deb http://www.igniterealtime.org/downloadServlet?filename=openfire/openfire_3.9.3_all.deb',
  require => Package['openjdk-7-jre']
}

exec { 'install-openfire':
  command => '/usr/bin/dpkg -i openfire.deb',
  user    => 'root',
  require => Exec['wget-openfire']
}