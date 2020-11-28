import psycopg2
import json

try:
    conn = psycopg2.connect(
        database="Trabalho_final",
        user="postgres",
        password="Left_4_dead*",
        host="localhost",
        port="5432"
    )
    print("Connected to postgresql DB")
except Exception as e:
    print(e)

cur = conn.cursor()

with open('../candidatos/120000633197.json', 'r') as c:
    candidates = json.load(c)

# apagar isso aqui depois
cur.execute("delete from candidato")
cur.execute("delete from partido")
cur.execute("delete from eleicao")
cur.execute("delete from cargo")
cur.execute("delete from bens")
cur.execute("delete from vices")
conn.commit()


def insert(table_name, value, fk='candidato'):
    column = ""
    values = ""

    for info in value:
        if type(value[info]) is list:
            # for x in value[info]:
            #     # print(info)
            #     print(fk)
            #     insert(info, x)
            continue

        elif type(value[info]) is dict:
            temp_table = value[info]
            temp_table_value = list(temp_table.keys())
            fk = info
            print(fk, info)
            insert(info, temp_table, info)
            column += "fk_%s, " % info
            values += "%s, " % str(temp_table[temp_table_value[0]])

        elif type(value[info]) == str:
            column += "%s, " % info
            values += "'%s', " % (value[info])

        elif value[info] is None:
            column += "%s, " % info
            values += "null, "

        else:
            column += info + ", "
            values += "%s, " % (str(value[info]))

    column = column[:-2]
    values = values[:-2]

    query = "INSERT INTO %s (%s) VALUES (%s);" % (table_name, column, values)
    try:
        cur.execute(query)
        print("Query to '%s' success\n" % table_name)
    except Exception as e:
        print(e)
    conn.commit()
    # print(column + "\n")
    # print(values)
    # print(query)


insert('candidato', candidates)
cur.close()
conn.close()
