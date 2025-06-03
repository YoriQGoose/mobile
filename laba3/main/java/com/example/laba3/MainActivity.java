import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLHelper db;
    private ListView listViewBooks;
    private ArrayList<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SQLHelper(this);
        listViewBooks = findViewById(R.id.listViewBooks);
        Button btnAddBook = findViewById(R.id.btnAddBook);

        loadBooks();

        btnAddBook.setOnClickListener(v -> showAddBookDialog());

        listViewBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = bookList.get(position);
            showEditDialog(selectedBook);
        });

        listViewBooks.setOnItemLongClickListener((parent, view, position, id) -> {
            Book selectedBook = bookList.get(position);
            db.deleteBook(selectedBook.id);
            loadBooks();
            Toast.makeText(this, "Книга удалена", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void loadBooks() {
        bookList.clear();
        Cursor cursor = db.getAllBooks();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                bookList.add(new Book(id, title, author, price));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getBookTitles());
        listViewBooks.setAdapter(adapter);
    }

    private ArrayList<String> getBookTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (Book book : bookList) {
            titles.add(book.title + "\nАвтор: " + book.author + "\nЦена: " + book.price + " руб.");
        }
        return titles;
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить книгу");

        View view = getLayoutInflater().inflate(R.layout.book_dialog, null);
        builder.setView(view);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String title = ((android.widget.EditText) view.findViewById(R.id.etTitle)).getText().toString();
            String author = ((android.widget.EditText) view.findViewById(R.id.etAuthor)).getText().toString();
            double price = Double.parseDouble(((android.widget.EditText) view.findViewById(R.id.etPrice)).getText().toString());

            db.addBook(title, author, price);
            loadBooks();
            Toast.makeText(this, "Книга добавлена", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showEditDialog(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Изменить цену");

        View view = getLayoutInflater().inflate(R.layout.price_dialog, null);
        android.widget.EditText etNewPrice = view.findViewById(R.id.etNewPrice);
        etNewPrice.setText(String.valueOf(book.price));
        builder.setView(view);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            double newPrice = Double.parseDouble(etNewPrice.getText().toString());
            db.updateBookPrice(book.id, newPrice);
            loadBooks();
            Toast.makeText(this, "Цена обновлена", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}