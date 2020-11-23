import psycopg2
import json

with open('credentials.json', 'r') as c:
    credentials = json.load(c)

try:
    conn = psycopg2.connect(
        database="Trabalho_final",
        user="postgres",
        password="admin123*",
        host="localhost",
        port="5432"
    )
    print("Connected to postgresql DB")
except Exception as e:
    print(e)

cur = conn.cursor()

with open('../candidatos/120000633197.json', 'r') as candidate:
    c_infos = json.load(candidate)


def check_attribute_and_type(attribute, value):
    attr_type = ''
    if type(value) == bool:
        attr_type = 'BOOLEAN NOT NULL'
    elif type(value) == int:
        attr_type = 'INTEGER NOT NULL'
    elif type(value) == str:
        attr_type = 'VARCHAR NOT NULL'
    elif type(value) == float:
        attr_type = 'FLOAT NOT NULL'
    elif value is None:
        attr_type = 'VARCHAR'
    elif type(value) is dict or type(value) is list:
        create_table(value, attribute)
        return ""
    else:
        return ""
    return attribute + " " + attr_type + ", "


def create_table(list_dict, table_name):
    to_query = ''

    # checa se é uma lista
    if type(list_dict) is list:
        if not (list_dict[0] is dict):
            query = "CREATE TABLE IF NOT EXISTS " + table_name + \
                " (id SERIAL, nome VARCHAR);"
            cur.execute(query)
            conn.commit()
            print("CREATED " + table_name + " TABLE")
            return
        else:
            list_dict = list_dict[0]

    for info in list_dict:
        to_query += check_attribute_and_type(info, list_dict[info])
    to_query = to_query[:-2]
    query = "CREATE TABLE IF NOT EXISTS " + table_name + " (" + to_query + ");"
    try:
        cur.execute(query)
        conn.commit()
        print("CREATED " + table_name + " TABLE")
    except:
        print("Query Failed")


create_table(c_infos, table_name="candidatos")


cur.close()
conn.close()
