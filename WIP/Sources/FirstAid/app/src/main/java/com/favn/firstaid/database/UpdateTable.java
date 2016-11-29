package com.favn.firstaid.database;

/**
 * Created by Hung Gia on 11/27/2016.
 */

public class UpdateTable {
    private Table[] update_table;

    public UpdateTable(Table[] update_table) {
        this.update_table = update_table;
    }

    public Table[] getUpdate_table() {
        return update_table;
    }

    public void setUpdate_table(Table[] update_table) {
        this.update_table = update_table;
    }

    class Table {
        private String table_name;
        private String updated_at;

        public Table(String table_name, String updated_at) {
            this.table_name = table_name;
            this.updated_at = updated_at;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
