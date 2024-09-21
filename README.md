# Fake person generator

Este é apenas um projeto brinquedo de uma API Restful capaz de gerar
pessoas fícticias a partir dos dados presentes em seu banco de dados.

Este projeto não possui mecanismos de validação, segurança, testes e estrutura para
ser utilizado seriamente de proposito. Sua principal finalidade foi testar
um pouco o OpenFeign em um projeto.

É possivel acessar e testar os endpoints do projeto por meio do Swagger
acessando a URL [Swagger](http://localhost:9999/swagger-ui/index.html).

A aplicação roda na porta ´9999´ e por hora ainda não foram adicionados endpoints
REST para adicionar 'Nomes' e 'Sobrenomes'.

Por hora os dados necessários para aplicação rodar vem de scripts sql presentes
na pasta sql/data/ em `resources`

É possível tambem usar o atributo 'seed' em alguns endpoints para controlar
os resultados gerados. As imagens dos perfis de usuário vem do serviço
'Dicebear' que disponibiliza uma API aberta para o uso, sem a necessidade de cadastro.


