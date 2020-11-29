import glob
import psycopg2
import json
import os

try:
    conn = psycopg2.connect(
        database="Trabalho_final",
        user="root",
        password="root",
        host="localhost",
        port="5432"
    )
    print("Connected to postgresql DB")
except Exception as e:
    print(e)

cur = conn.cursor()


def insert(table_name, value, do_after):
    column = ""
    values = ""

    for column_name in value:
        if type(value[column_name]) is list:
            temp_dict = {column_name: value[column_name]}
            do_after.append(temp_dict)

        elif type(value[column_name]) is dict:
            temp_table = value[column_name]
            temp_table_value = list(temp_table.keys())
            insert(column_name, temp_table, do_after)
            column += "fk_%s, " % column_name
            values += "%s, " % str(temp_table[temp_table_value[0]])

        elif type(value[column_name]) == str:
            column += "%s, " % column_name
            value[column_name] = value[column_name].replace("'", "`")
            values += "'%s', " % (value[column_name])

        elif value[column_name] is None:
            # Exception
            if column_name == "vices" or column_name == "motivos" or column_name == "substituto":
                break
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
    except Exception as e:
        e = str(e)
        if e.find('duplicate key value violates unique constraint "cargo_pkey"'):
            pass
        else:
            print(e)
    conn.commit()


def insert_if_list(list1):
    for i in range(len(list1)):
        temp_obj = list1[i]
        for temp_json in temp_obj:
            i = temp_obj[temp_json]
            for x in i:
                column = ""
                values = ""
                for y in x:
                    # print(x, y)
                    if temp_json == 'emails':
                        column += "descricao, "
                        values += "'%s', " % x
                        break
                    elif temp_json == 'sites':
                        column += "descricao, "
                        values += "'%s', " % x
                        break

                    elif temp_json == 'motivos':
                        column += "descricao, "
                        values += "'%s', " % x
                        break

                    elif type(x[y]) == str:
                        column += "%s, " % y
                        x[y] = x[y].replace("'", "`")
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
                except Exception as e:
                    e = str(e)
                    if e.find('duplicate key value violates unique constraint "cargo_pkey"'):
                        pass
                    else:
                        print(e)

                conn.commit()


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

folder_path = 'C:/Users/yanaj/Minhas_coisa/Facul/2020_02/LBD/Trabalho_final/candidatos'
for filename in glob.glob(os.path.join(folder_path, '*.json')):
    with open(filename, 'r') as c:
        candidates = json.load(c)
        do_after = []
        print(filename)
        insert('candidato', candidates, do_after)
        insert_if_list(do_after)

# with open('../candidatos/120000633197.json', 'r') as c:
#     candidates = json.load(c)
#     do_after = []
#     insert('candidato', candidates, do_after)
#     insert_if_list(do_after)

cur.close()
conn.close()
