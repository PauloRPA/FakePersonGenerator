# Fake person generator

## Introdução

Este é apenas um projeto brinquedo de uma API Restful capaz de gerar
pessoas fictícias a partir dos dados presentes em seu banco de dados.

Este projeto propositalmente não possui mecanismos de validação, segurança, testes e estrutura para
ser utilizado seriamente. Sua principal finalidade foi testar um pouco o OpenFeign e o gradle em um projeto.

Os dados necessários para aplicação rodar vem de scripts sql presentes
na pasta sql/data/ em `resources`. Caso necessário, 'Name', 'Lastname' e 'Region' podem ser adicionados
 por meio dos endpoints rest disponíveis. O objeto 'Person' não possui persistência
 e deve ser gerado. O usuário pode, opcionalmente, controlar a geração de
 uma pessoa por meio da seed usada para geração. Ao utilizar uma seed o resultado da geração será
 sempre consistente com a seed.

As imagens dos perfis de usuário vem do serviço 'Dicebear' que disponibiliza
 uma API aberta para o uso, sem a necessidade de cadastro. O acesso à API é feito
 usando o OpenFeign.

## Instalação

1. Primeiro clone o repositório ```git clone https://github.com/PauloRPA/FakePersonGenerator```
2. Entre na pasta do diretório e rode ```gradle bootRun``` isso iniciará a aplicação.
3. A aplicação iniciará na porta `9999`. 
4. Por ser uma aplicação de brinquedo o banco de dados padrão é em memória (H2).

É possível acessar e testar os endpoints do projeto por meio do Swagger
acessando o caminho ```/swagger-ui/index.html``` ou use a URL [Swagger](http://localhost:9999/swagger-ui/index.html).