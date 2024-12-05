INSERT INTO Escola (id, nome, cnpj, telefone, estado, cidade) VALUES
(1, 'Escola Estadual de Minas', '22.333.444/0001-55', '31 1234-5678', 'Minas Gerais', 'Belo Horizonte');

INSERT INTO Usuario (id, user, senha) VALUES
(1, 'responsavel1', UNHEX(SHA2('senha123', 256))),
(2, 'aluno1', UNHEX(SHA2('senha123', 256))),
(3, 'adminEscola1', UNHEX(SHA2('senha123', 256))),
(4, 'admin1', UNHEX(SHA2('senha123', 256))),
(5, 'responsavel2', UNHEX(SHA2('senha123', 256))),
(6, 'aluno2', UNHEX(SHA2('senha123', 256)));

INSERT INTO Responsavel (id, nome, cpf) VALUES
(1, 'Jonathan Silva', '123.456.789-00'),
(5, 'Maria Fernandes', '987.654.321-00');

INSERT INTO Aluno (id, responsavel_id, escola_id, nome, matricula) VALUES
(2, 1, 1, 'Maria Souza', '2021001'),
(6, 5, 1, 'Lucas Fernandes', '2021002');

INSERT INTO Admin (id) VALUES (4);
INSERT INTO Admin_Escola (id, escola_id) VALUES (3, 1);

INSERT INTO Item_Estoque_Cantina (id, disponivel, nome, preco, tipo, escola_id) VALUES
(1, 1, 'Coxinha', 5.00, 'SALGADO', 1),
(2, 1, 'Pão de Queijo', 2.00, 'SALGADO', 1),
(3, 1, 'Bolo de Chocolate', 3.00, 'DOCE', 1);

INSERT INTO Conta 
    (id, responsavel_id, aluno_id, saldo, limite_diario, limite_diario_padrao, data_ultimo_pedido, tem_limite_diario) 
VALUES
    (1, 1, 2, 12.50, 150.00, 150.00, '2024-04-01', TRUE),
    (2, 5, 6, 15.00, 250.00, 250.00, '2024-04-02', FALSE);

INSERT INTO Pedido (preco_total, data, status, conta_id) VALUES
(5.00, NOW(), 'Pendente', 1), 
(10.00, NOW(), 'Pendente', 2), 
(15.00, NOW(), 'Pendente', 2); 

INSERT INTO Item_Pedido (pedido_id, item_estoque_id, quantidade, valor_total) VALUES
(1, 1, 1, 5.00),
(2, 2, 2, 4.00),
(3, 3, 3, 9.00);
