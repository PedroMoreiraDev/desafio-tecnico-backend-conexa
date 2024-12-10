# Desafio Técnico backend Conexa

Construir uma API REST para que nossos médicos de plantão consigam se logar na plataforma e agendar atendimentos para pacientes.

## Autenticação

### Signup
Precisamos de uma rota para que os médicos consigam realizar cadastro na plataforma:
```
endpoint:  /api/v1/signup
{
  "email": "medico@email.com",
  "senha": "",
  "confirmacaoSenha": "",
  "especialidade": "Cardiologista",
  "cpf": "101.202.303-11",
  "dataNascimento": "10/03/1980",
  "telefone": "(21) 3232-6565"
}
```

### Login
Precisamos de uma rota para que médicos da clínica Conexa Saúde consigam realizar login na aplicação:
```
Request:
  POST /api/v1/login
  {
    "email": "medico@email.com",
    "senha": "senhamedico"
  }

Response:
  Status-Code: 200
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
  }
```

### Logoff
Também precisamos de uma rota para o médico conseguir realizar logoff:
```
endpoint: /api/v1/logoff
Authorization: token_jwt
```

### Requisitos para autenticação:
- Utilização de JWT;
- Após logoff, o token precisa ser invalidado, não podendo mais ser permitida a sua utilização;
- Validações de tipos de campo como E-mail, CPF válido, idade apenas números, telefone válido, etc;
- As senhas precisam ser armazenadas de forma criptografada;

## Agendamento

### Criação de atendimento
Precisamos de uma rota onde o médico logado realiza agendamento de consulta para um paciente:
```
endpoint: /api/v1/attendance
Authorization: token_jwt
{
  "dataHora": "2020-08-03 09:00:00",
  "paciente": {
    "nome": "João Castro",
    "cpf": "101.202.303"
  }
}
```

### Requisitos para atendimentos:
- Todas as rotas de atendimento precisam passar pela autenticação;
- Só pode ser possível criar agendamentos no futuro;


# Requisitos Obrigatórios:
- Utilização de Java com Spring;
- Banco de dados MySQL;
- Realizar fork deste repositório, e após o desenvolvimento, abrir um merge request apontando para repositório original;


# Serão avaliados os seguintes itens:
* Se todos os requisitos descritos acima foram atendidos;
* Clareza do código;
* Estrutura de código organizada;
* Se utilizou corretamente os conceitos do REST;
* Se utilizou o Spring de forma correta;
* Se possui tratamento de erros;
* Se os testes rodam;

**Obs:**
Necessário descrever, ou prover, todas as dependências e ações necessárias (banco, tabelas, versões, etc) para que o avaliador consiga rodar a aplicação na sua máquina local;

**Lembre-se que uma aplicação boa é uma aplicação bem testada;**

Em caso de dúvidas sobre o desafio, entre em contato.

=======================================================================

Para testar:
Acesse a pasta principal do projeto

execute o comando ```docker-compose up --build -d```

utilize os endpoints fornecidos apontando para o localhost 9090.

Lembre-se de modificar o token quando necessário.

````
curl --location 'http://localhost:9090/api/v1/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "medic2o@email.com",
  "senha": "teste111",
  "confirmacaoSenha": "teste111",
  "especialidade": "Cardiologista",
  "cpf": "438.995.278-10",
  "dataNascimento": "10/03/1980",
  "telefone": "(21) 3232-6565"
}'
````


````
curl --location 'http://localhost:9090/api/v1/login' \
--header 'Content-Type: application/json' \
--data-raw ' {
    "email": "medic2o@email.com",
    "senha": "teste111"
  }'
````

````
curl --location 'http://localhost:9090/api/v1/attendance' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6Im1lZGljMm9AZW1haWwuY29tIiwiZXhwIjoxNzMzNzY0MjkxfQ.edkmP_Av0NPQSISuU57SnyZ3qpz1E57r3ODDx0AwxNI' \
--header 'Content-Type: application/json' \
--data '{
  "dataHora": "2025-08-05 11:04:00",
  "paciente": {
    "nome": "João Castro",
    "cpf": "438.995.278-10"
  }
}'
````
````
curl --location --request POST 'http://localhost:9090/api/v1/logoff' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6Im1lZGljMm9AZW1haWwuY29tIiwiZXhwIjoxNzMzNzI2NTkxfQ.qhE0zHku_bCpBZ6j7bondNbLd7bhQRfFdgoou_JikMc'
````


