
CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          nome_completo VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE

);

CREATE TABLE contas_financeiras (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    nome VARCHAR(255) NOT NULL,
                                    tipo_conta VARCHAR(100),
                                    saldo_inicial DECIMAL(19, 2) DEFAULT 0.00,
                                    usuario_id BIGINT NOT NULL,
                                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE categorias (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            nome VARCHAR(255) NOT NULL,
                            tipo VARCHAR(50) NOT NULL, -- 'RECEITA', 'DESPESA'
                            usuario_id BIGINT NOT NULL,
                            FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                            UNIQUE(nome, usuario_id, tipo)
);

CREATE TABLE transacoes (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            descricao VARCHAR(255) NOT NULL,
                            valor DECIMAL(19, 2) NOT NULL,
                            data DATE NOT NULL,
                            tipo VARCHAR(50) NOT NULL,
                            conta_financeira_id BIGINT NOT NULL,
                            categoria_id BIGINT NOT NULL,
                            usuario_id BIGINT NOT NULL,
                            data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                            FOREIGN KEY (conta_financeira_id) REFERENCES contas_financeiras(id),
                            FOREIGN KEY (categoria_id) REFERENCES categorias(id),
                            FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE

);


CREATE TABLE objetivos_financeiros (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nome VARCHAR(255) NOT NULL,
                                       valor_alvo DECIMAL(19, 2) NOT NULL,
                                       prazo DATE,
                                       progresso_atual DECIMAL(19, 2) DEFAULT 0.00,
                                       usuario_id BIGINT NOT NULL,
                                       FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE aportes_objetivos (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   valor DECIMAL(19, 2) NOT NULL,
                                   data DATE NOT NULL,
                                   objetivo_financeiro_id BIGINT NOT NULL,
                                   FOREIGN KEY (objetivo_financeiro_id) REFERENCES objetivos_financeiros(id) ON DELETE CASCADE
);

CREATE TABLE investimentos_pessoais (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        nome VARCHAR(255) NOT NULL,
                                        tipo_investimento VARCHAR(100),
                                        valor_atual DECIMAL(19, 2) NOT NULL,
                                        data_ultima_atualizacao DATE,
                                        usuario_id BIGINT NOT NULL,
                                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE orcamentos_projetos (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     nome VARCHAR(255) NOT NULL,
                                     limite_gastos DECIMAL(19, 2) NOT NULL,
                                     data_inicio DATE,
                                     data_fim DATE,
                                     usuario_id BIGINT NOT NULL,
                                     FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE clientes_mei (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nome VARCHAR(255) NOT NULL,
                              cnpj_cpf VARCHAR(20) UNIQUE,
                              email VARCHAR(255),
                              telefone VARCHAR(20),
                              usuario_id BIGINT NOT NULL, -- O MEI que cadastrou o cliente
                              FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE vendas_mei (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            descricao TEXT,
                            valor DECIMAL(19, 2) NOT NULL,
                            data_venda DATE NOT NULL,
                            data_vencimento DATE,
                            status_pagamento VARCHAR(50) NOT NULL,
                            cliente_mei_id BIGINT,
                            usuario_id BIGINT NOT NULL,
                            FOREIGN KEY (cliente_mei_id) REFERENCES clientes_mei(id) ON DELETE SET NULL,
                            FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE das_pagamentos (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                mes_referencia INT NOT NULL,
                                ano_referencia INT NOT NULL,
                                valor DECIMAL(19, 2) NOT NULL,
                                data_vencimento DATE NOT NULL,
                                pago BOOLEAN DEFAULT FALSE,
                                usuario_id BIGINT NOT NULL,
                                UNIQUE(mes_referencia, ano_referencia, usuario_id),
                                FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);


CREATE TABLE clientes_diarista (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   nome VARCHAR(255) NOT NULL,
                                   endereco TEXT,
                                   latitude DOUBLE,
                                   longitude DOUBLE,
                                   contato VARCHAR(100),
                                   usuario_id BIGINT NOT NULL,
                                   FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE servicos_agendados (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    descricao TEXT,
                                    data_hora_servico TIMESTAMP NOT NULL,
                                    valor_cobrado DECIMAL(19, 2) NOT NULL,
                                    status VARCHAR(50) NOT NULL,
                                    cliente_diarista_id BIGINT,
                                    usuario_id BIGINT NOT NULL,
                                    FOREIGN KEY (cliente_diarista_id) REFERENCES clientes_diarista(id) ON DELETE SET NULL,
                                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

