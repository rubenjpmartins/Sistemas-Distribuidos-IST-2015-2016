# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 38 - Campus Tagus-Park

Duarte Clara 76832 duartenvclara@gmail.com

Rúben Martins 79532 rubenjpmartins@gmail.com


Repositório:
[tecnico-softeng-distsys-2015/T_38-project](https://github.com/tecnico-softeng-distsys-2015/T_38-project/)

-------------------------------------------------------------------------------

## Instruções de instalação

### Ambiente

[0] Iniciar sistema operativo
```
Linux
```

[1] Iniciar servidores de apoio

JUDDI:
```
É usado o Servidor de nomes para Web Services que segue a norma UDDI 
->jUDDI 3.3.2 (configured for port 9090)

Tornar os scripts de lançamento executáveis:
chmod +x juddi-3.3.2_tomcat-7.0.64_9090/bin/*.sh

Para lançar o servidor, basta executar o seguinte comando na pasta 
juddi-3.3.2_tomcat-7.0.64_9090/bin:

./startup.sh

Para confirmar funcionamento, aceder à página de índice do jUDDI, que dá também acesso à 
interface de administração:
http://localhost:9090/juddiv3/

utilizador: uddiadmin
senha: da_password1
```

[2] Criar pasta temporária

```
cd Documents
mkdir SDProj
cd SDProj
```

[3] Obter código fonte do projeto (versão entregue)

```
Versão da entrega da 1ª parte

git clone -b SD_R1 https://github.com/tecnico-softeng-distsys-2015/T_38-project/

cd T_38-project
```

```
Versão da entrega da 2ª parte

git clone -b SD_R2 https://github.com/tecnico-softeng-distsys-2015/T_38-project/

cd T_38-project
```




[4] Iniciar CERTIFICATE AUTHORITY SERVICE:

```
cd cas-ws
mvn clean
mvn generate-sources
mvn compile
mvn exec:java
```

[5] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

```
cd cripto
mvn clean install
```

```
cd cas-ws-cli
mvn clean install 
```

```
cd ws-handlers
mvn clean install 
```

```
cd transporter-ws-cli
mvn clean install -DskipTests
```

-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd transporter-ws
mvn clean 
mvn generate-sources
mvn compile
mvn test
mvn exec:java

Para correr instâncias diferentes do serviço Transporter:
mvn -Dws.i=2 exec:java   (onde 2representa o número da transportadora a lançar)
```

[2] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn clean 
mvn generate-sources
mvn compile
mvn test
mvn verify
```

-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor primário**

```
cd broker-ws
mvn clean 
mvn generate-sources
mvn compile
mvn exec:java
```

[2] Construir e executar **servidor secundário**

```
cd broker-ws
mvn clean 
mvn generate-sources
mvn compile
mvn -Dw.i=2 exec:java
```

[3] Construir **cliente** e executar testes

```
cd broker-ws-cli
mvn clean
mvn generate-sources
mvn compile
mvn test
mvn verify
```
-------------------------------------------------------------------------------
**FIM**
