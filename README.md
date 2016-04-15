# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 38 - Campus Tagus-Park

Duarte Clara 76832 duartenvclara@gmail.com

Rúben Martins 79532 rubenjpmartins@gmail.com

João Pires 76412 joao_pires_pires@hotmail.com 

Repositório:
[tecnico-softeng-distsys-2015/T_38-project](https://github.com/tecnico-softeng-distsys-2015/T_38-project/)

-------------------------------------------------------------------------------

## Instruções de instalação


### Ambiente

[0] Iniciar sistema operativo

Linux


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
```


[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

```
cd ...
mvn clean install
```


-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd ...-ws
mvn clean install
mvn exec:java
```

[2] Construir **cliente** e executar testes

```
cd ...-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd ...-ws
mvn clean install
mvn exec:java
```


[2] Construir **cliente** e executar testes

```
cd ...-ws-cli
mvn clean install
```

...

-------------------------------------------------------------------------------
**FIM**
