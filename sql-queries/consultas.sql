-- Consulta 1
SELECT COUNT(c.id) quantidade, p.nome
FROM candidatos c INNER JOIN partidos p ON p.numero = c.fk_partido
GROUP by c.fk_partido, p.nome ORDER BY quantidade desc LIMIT 1;

-- Consulta 2
SELECT COUNT(descricaosexo), descricaosexo 
FROM candidatos GROUP BY descricaosexo;

-- Consulta 3
SELECT COUNT(descricaocorraca) quantidade, descricaocorraca
FROM candidatos GROUP BY descricaocorraca ORDER BY quantidade DESC;

-- Consulta 4
SELECT COUNT(cb.ordem) bens, c.nomeurna FROM candidato_bens cb
INNER JOIN candidatos c ON c.id = cb.fk_candidato GROUP BY c.nomeurna ORDER BY bens DESC LIMIT 1;

-- Consulta 5
SELECT datadenascimento, nomeurna from candidatos ORDER BY datadenascimento, nomeurna limit 10

-- Consulta 6
SELECT ordem, valor FROM candidato_bens ORDER BY valor DESC LIMIT 10
