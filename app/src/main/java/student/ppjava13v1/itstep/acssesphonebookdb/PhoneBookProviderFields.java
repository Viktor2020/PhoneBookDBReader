package student.ppjava13v1.itstep.acssesphonebookdb;

import android.net.Uri;

public interface PhoneBookProviderFields {
     String AUTHORITY = "student.ppjava13v1.itstep.phonebook.database.DBContentProvider";
     String PATH = PhoneBookColumns.CONTACT_TABLE;
     Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
}
