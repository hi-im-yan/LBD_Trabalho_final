-- Consulta 1
SELECT COUNT(p.numero) quantidade, p.nome as nome_partido, p.numero as numero_partido
FROM partido p
INNER JOIN candidato c ON c.fk_partido = p.numero
GROUP by numero_partido ORDER BY quantidade DESC
LIMIT 1;

-- Consulta 2
SELECT COUNT(descricaosexo), descricaosexo 
FROM candidato 
GROUP BY descricaosexo;

-- Consulta 3
SELECT COUNT(descricaocorraca) quantidade, descricaocorraca
FROM candidato
GROUP BY descricaocorraca ORDER BY quantidade DESC;

-- Consulta 4
SELECT COUNT(b.ordem) bens, c.nomeUrna
FROM bens b
INNER JOIN candidato c ON b.FK_candidato = c.id
GROUP BY c.nomeUrna ORDER BY bens DESC
LIMIT 1;

-- Consulta 5
select datadenascimento, nomeUrna
from candidato
ORDER BY datadenascimento, nomeUrna
limit 10

-- Consulta 6
SELECT ordem, valor
FROM bens
ORDER BY valor DESC
LIMIT 10
