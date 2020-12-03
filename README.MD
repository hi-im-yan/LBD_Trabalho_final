##### Link do vídeo
https://youtu.be/s8G8BEbJvsU

##### Integrantes
Rafael Camilo da Silva Melges - 201819070816
Yan Ajiki - 201819061086
André Diógenes Silva - 201719060975

##### COMO RODAR A APLICAÇÃO E GERAR O SQL

###### Linux || MACOS
entre na pasta raiz do projeto e execute o seguinte comando:
    - sudo sh -c "chmod +x ./scripts/init-import && sudo ./scripts/init-import"
    - OBS: COM AS ASPAS DUPLAS

###### WINDOWS
- libere a porta 8457
- rode o comando:
    - docker-compose up --build && docker logs -f trabalho-sqlge
    - quando o processamento acabar rode os seguintes comandos
    - ```docker exec -it trabalho-db pg_dump -U root -h localhost trabalho_db >> sql-queries/rafael-camilo_yan-ajiki_andre-diogenes.sql```
    - ```cd sql-queries```
    - O nome do arquivo é rafael-camilo_yan-ajiki_andre-diogenes.sql está dentro da pasta sql-queries
