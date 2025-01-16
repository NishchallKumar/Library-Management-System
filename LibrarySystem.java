import java.util.*;

class Book {
    int id;
    String title, author, genre;
    boolean isAvailable;

    public Book(int id, String title, String author, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }

    
    public String toString() {
        return String.format("ID: %d, Title: %s, Author: %s, Genre: %s, Available: %s", 
                              id, title, author, genre, isAvailable ? "Yes" : "No");
    }
}

class User {
    int userId;
    String name;
    Map<Integer, Long> borrowedBooks; // Book ID with issue timestamp

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowedBooks = new HashMap<>();
    }

    
    public String toString() {
        return String.format("User ID: %d, Name: %s, Borrowed Books: %s", userId, name, borrowedBooks.keySet());
    }
}

class LibraryManagementSystem {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, User> users = new HashMap<>();
    private static final int FINE_PER_DAY = 5;

    public void addBook(Book book) {
        books.put(book.id, book);
        System.out.println("Book added: " + book.title);
    }

    public void removeBook(int bookId) {
        if (books.remove(bookId) != null) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void addUser(User user) {
        users.put(user.userId, user);
        System.out.println("User added: " + user.name);
    }

    public void issueBook(int userId, int bookId) {
        User user = users.get(userId);
        Book book = books.get(bookId);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable) {
            System.out.println("Book is not available.");
            return;
        }

        book.isAvailable = false;
        user.borrowedBooks.put(bookId, System.currentTimeMillis());
        System.out.println("Book issued successfully.");
    }

    public void returnBook(int userId, int bookId) {
        User user = users.get(userId);
        Book book = books.get(bookId);

        if (user == null || !user.borrowedBooks.containsKey(bookId)) {
            System.out.println("Invalid return request.");
            return;
        }

        long issueTime = user.borrowedBooks.get(bookId);
        long overdueDays = (System.currentTimeMillis() - issueTime) / (1000 * 60 * 60 * 24);
        long fine = overdueDays > 14 ? (overdueDays - 14) * FINE_PER_DAY : 0;

        book.isAvailable = true;
        user.borrowedBooks.remove(bookId);
        System.out.println("Book returned successfully. Fine: ₹" + fine);
    }

    public void viewBooks() {
        books.values().forEach(System.out::println);
    }

    public void viewUsers() {
        users.values().forEach(System.out::println);
    }
}

public class LibrarySystem {
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();

        // Sample data
        library.addBook(new Book(1, "The Alchemist", "Paulo Coelho", "Fiction"));
        library.addBook(new Book(2, "Clean Code", "Robert C. Martin", "Programming"));
        library.addUser(new User(1, "Alice"));
        library.addUser(new User(2, "Bob"));

        // Menu
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Book\n2. Remove Book\n3. Issue Book\n4. Return Book\n5. View Books\n6. View Users\n7. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter book ID, title, author, genre: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    String title = sc.nextLine();
                    String author = sc.nextLine();
                    String genre = sc.nextLine();
                    library.addBook(new Book(id, title, author, genre));
                    break;
                case 2:
                    System.out.print("Enter book ID to remove: ");
                    int bookId = sc.nextInt();
                    library.removeBook(bookId);
                    break;
                case 3:
                    System.out.print("Enter user ID and book ID to issue: ");
                    int userId = sc.nextInt();
                    bookId = sc.nextInt();
                    library.issueBook(userId, bookId);
                    break;
                case 4:
                    System.out.print("Enter user ID and book ID to return: ");
                    userId = sc.nextInt();
                    bookId = sc.nextInt();
                    library.returnBook(userId, bookId);
                    break;
                case 5:
                    library.viewBooks();
                    break;
                case 6:
                    library.viewUsers();
                    break;
                case 7:
                    System.out.println("Exiting system. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
