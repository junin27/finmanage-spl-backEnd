INSERT INTO usuarios (username, password, nome_completo, email) VALUES
                                                                    ('user_pessoal', '$2a$10$GRLqNJPJv.7m2gUo2wNa8uL40owexK5gUqYn5d0e030Nl3q7u0m0e', 'Usuário Pessoal Teste', 'pessoal@example.com'),
                                                                    ('user_mei', '$2a$10$GRLqNJPJv.7m2gUo2wNa8uL40owexK5gUqYn5d0e030Nl3q7u0m0e', 'Usuário MEI Teste', 'mei@example.com'),
                                                                    ('user_diarista', '$2a$10$GRLqNJPJv.7m2gUo2wNa8uL40owexK5gUqYn5d0e030Nl3q7u0m0e', 'Usuário Diarista Teste', 'diarista@example.com');


INSERT INTO categorias (nome, tipo, usuario_id) VALUES
                                                    ('Salário', 'RECEITA', 1),
                                                    ('Alimentação', 'DESPESA', 1),
                                                    ('Transporte', 'DESPESA', 1),
                                                    ('Moradia', 'DESPESA', 1);


INSERT INTO contas_financeiras (nome, tipo_conta, saldo_inicial, usuario_id) VALUES
                                                                                 ('Carteira Pessoal', 'CARTEIRA', 50.00, 1),
                                                                                 ('Conta Corrente Pessoal', 'CONTA_CORRENTE', 1000.00, 1);


INSERT INTO transacoes (descricao, valor, data, tipo, conta_financeira_id, categoria_id, usuario_id) VALUES
                                                                                                         ('Recebimento Salário Mês', 5000.00, '2025-06-01', 'RECEITA', 2, 1, 1),
                                                                                                         ('Almoço Restaurante', 35.00, '2025-06-01', 'DESPESA', 1, 2, 1);

