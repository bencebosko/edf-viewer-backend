keytool -genkeypair -alias cert \
        -keyalg RSA \
        -keysize 4096 \
        -keystore cert \
        -storetype PKCS12 \
        -storepass password \
        -validity 9999 \
        -dname "CN=bbsoft.com C=hungary L=budapest ST=budapest"
