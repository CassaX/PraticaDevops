CREATE TABLE IF NOT EXISTS pessoa (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  idade INT NOT NULL,
  cpf VARCHAR(20) NOT NULL,
  telefone VARCHAR(20),
  email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS logs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  mensagem TEXT NOT NULL,
  data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO pessoa (nome, idade, cpf, telefone, email) VALUES
('João Silva', 30, '123.456.789-00', '11999999999', 'joao.silva@email.com'),
('Maria Oliveira', 25, '987.654.321-00', '11988888888', 'maria.oliveira@email.com');

INSERT INTO logs (mensagem) VALUES
('Sistema iniciado'),
('Primeira pessoa cadastrada');
