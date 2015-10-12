package student.ppjava13v1.itstep.acssesphonebookdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CursorAdapter adapter;
    private Cursor cursor;
    private EditText number;
    private TextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = getAdapter();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        autoCompleteTextView = (TextView) findViewById(R.id.et_name);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(MainActivity.this.getCurrentFocus());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        number = (EditText) findViewById(R.id.et_number);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(MainActivity.this.getCurrentFocus());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search(this.getCurrentFocus());
    }

    private CursorAdapter getAdapter() {
        return  new SimpleCursorAdapter(getApplicationContext()
                , R.layout.item
                , cursor
                , new String[] { PhoneBookColumns._ID
                , PhoneBookColumns.CONTACT_NAME_COLUMN
                , PhoneBookColumns.CONTACT_NUMBER_COLUMN }
                , new int[]{R.id.item_id, R.id.item_name, R.id.item_number}
                , CursorAdapter.NO_SELECTION);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        Log.wtf("onCreateContextMenu", menuInfo.toString());

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_update:
                update(info.id);
                return true;
            case R.id.menu_delete:
                delete(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(View view) {
        String name = this.autoCompleteTextView.getText().toString() + "%";
        String number = this.number.getText().toString() + "%";

        String selection = PhoneBookColumns.CONTACT_NAME_COLUMN + " LIKE ? " +
                " AND " + PhoneBookColumns.CONTACT_NUMBER_COLUMN + " LIKE ? ";
        String selectionName = PhoneBookColumns.CONTACT_NAME_COLUMN + " LIKE ? ";
        String selectionNumb = PhoneBookColumns.CONTACT_NUMBER_COLUMN + " LIKE ? ";

        String[] args = {name, number};

        if (name.equals("") && number.equals("")) {
            args = null;
            selection = null;
        } else if (name.equals("")) {
            args = new String[]{number};
            selection = selectionNumb;
        } else if (number.equals("")) {
            args = new String[]{name};
            selection = selectionName;
        }

        cursor = getContentResolver().query(PhoneBookProviderFields.CONTENT_URI, null
                , selection, args, null);

        adapter.changeCursor(cursor);

    }

    public void delete(long id) {
        getContentResolver().delete(PhoneBookProviderFields.CONTENT_URI
                , PhoneBookColumns._ID + "=?", new String[]{String.valueOf(id)});
    }

    public void update(long id) {
        ContentValues cv = getContentValue();
        getContentResolver().update(PhoneBookProviderFields.CONTENT_URI, cv
                , PhoneBookColumns._ID + "=?", new String[]{String.valueOf(id)});
    }

    public void insert(View view) {
        ContentValues cv = getContentValue();
        getContentResolver().insert(PhoneBookProviderFields.CONTENT_URI, cv);
    }

    private ContentValues getContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(PhoneBookColumns.CONTACT_NAME_COLUMN, autoCompleteTextView.getText().toString());
        cv.put(PhoneBookColumns.CONTACT_NUMBER_COLUMN, number.getText().toString());
        return cv;
    }
}
