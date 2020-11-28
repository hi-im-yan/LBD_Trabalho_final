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
cur.execute("delete from partido")
cur.execute("delete from eleicao")
cur.execute("delete from cargo")
cur.execute("delete from bens cascade")
cur.execute("delete from vices cascade")
cur.execute("delete from arquivos cascade")
cur.execute("delete from emails cascade")
cur.execute("delete from sites cascade")
cur.execute("delete from eleicoesanteriores cascade")
cur.execute("delete from candidato")
conn.commit()

do_after = []


def insert(table_name, value):
    column = ""
    values = ""

    for column_name in value:
        if type(value[column_name]) is list:
            temp_dict = {column_name: value[column_name]}
            do_after.append(temp_dict)
            # column += "fk_candidato, "
            # values += "%s, " % str(candidates['id'])
            # print(value[column_name], type(column_name))
            # for x in value[column_name]:
            #     insert(column_name, x, 'candidato')

        elif type(value[column_name]) is dict:
            temp_table = value[column_name]
            temp_table_value = list(temp_table.keys())
            insert(column_name, temp_table)
            column += "fk_%s, " % column_name
            values += "%s, " % str(temp_table[temp_table_value[0]])

        elif type(value[column_name]) == str:
            column += "%s, " % column_name
            values += "'%s', " % (value[column_name])

        elif value[column_name] is None:
            column += "%s, " % column_name
            values += "null, "

        else:
            column += column_name + ", "
            values += "%s, " % (str(value[column_name]))

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


def insert_if_list(list1):
    for i in range(len(list1)):
        temp_obj = list1[i]
        for temp_json in temp_obj:
            i = temp_obj[temp_json]

            for x in i:
                column = ""
                values = ""
                for y in x:
                    if temp_json == 'emails':
                        column += "descricao, "
                        values += "'%s', " % x
                        break
                    elif temp_json == 'sites':
                        column += "descricao, "
                        values += "'%s', " % x
                        break

                    elif type(x[y]) == str:
                        column += "%s, " % y
                        values += "'%s', " % (x[y])

                    elif x[y] is None:
                        column += "%s, " % y
                        values += "null, "

                    else:
                        column += y + ", "
                        values += "%s, " % (str(x[y]))

                column += "fk_candidato"
                values += str(candidates['id'])

                query = "INSERT INTO %s (%s) VALUES (%s);" % (
                    temp_json, column, values)
                try:
                    cur.execute(query)
                    print("Query to '%s' success\n" % temp_json)
                except Exception as e:
                    print(e)

                conn.commit()


insert_if_list(do_after)

cur.close()
conn.close()
