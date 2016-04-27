# commands required to generate key pair and public key digital certificate
keytool -genkeypair -alias "UpaTransporter1" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r1" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter2" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r2" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter3" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r3" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter4" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r4" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter5" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r5" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter6" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r6" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter7" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r7" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter8" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r8" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -genkeypair -alias "UpaTransporter9" -keyalg RSA -keysize 2048 -keypass "UpaTransport3r9" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
#broker
keytool -genkeypair -alias "UpaBroker" -keyalg RSA -keysize 2048 -keypass "UpaBrok3r" -validity 90 -storepass "SDiskeyst" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
#Cria Certificados
keytool -export -keystore keystore.jks -alias UpaTransporter1 -storepass "SDiskeyst" -file UpaTransporter1.cer
keytool -export -keystore keystore.jks -alias UpaTransporter2 -storepass "SDiskeyst" -file UpaTransporter2.cer
keytool -export -keystore keystore.jks -alias UpaTransporter3 -storepass "SDiskeyst" -file UpaTransporter3.cer
keytool -export -keystore keystore.jks -alias UpaTransporter4 -storepass "SDiskeyst" -file UpaTransporter4.cer
keytool -export -keystore keystore.jks -alias UpaTransporter5 -storepass "SDiskeyst" -file UpaTransporter5.cer
keytool -export -keystore keystore.jks -alias UpaTransporter6 -storepass "SDiskeyst" -file UpaTransporter6.cer
keytool -export -keystore keystore.jks -alias UpaTransporter7 -storepass "SDiskeyst" -file UpaTransporter7.cer
keytool -export -keystore keystore.jks -alias UpaTransporter8 -storepass "SDiskeyst" -file UpaTransporter8.cer
keytool -export -keystore keystore.jks -alias UpaTransporter9 -storepass "SDiskeyst" -file UpaTransporter9.cer
keytool -export -keystore keystore.jks -alias UpaBroker -storepass "SDiskeyst" -file UpaBroker.cer
