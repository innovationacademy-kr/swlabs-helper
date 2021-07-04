# swlabs-helper

helper finder

## Setup if you dev

### env

This is for oauth setting

you need to add 42api and at least scope include public option!

- ${UID42}
- ${SECRET42}

This project use spring-security so you need to set redirect_url like this

``{baseUrl}:{port}/login/oauth2/intra42``

This project support h2 embedded db for local

but if you want to use -dev.properties for mariadb setting

please use mariadb10.3 or over version and regist env below

- ${HELPER42_DB_NAME}
- ${HELPER42_DB_USERNAME}
- ${HELPER42_DB_PASSWORD}