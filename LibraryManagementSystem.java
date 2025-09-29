
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Book class representing a library book
class Book {

    private String title;
    private String author;
    private String borrower;
    private LocalDate borrowDate;
    private boolean isAvailable;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.borrower = null;
        this.borrowDate = null;
        this.isAvailable = true;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBorrower() {
        return borrower;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Borrow the book
    public boolean borrowBook(String borrowerName) {
        if (isAvailable) {
            this.borrower = borrowerName;
            this.borrowDate = LocalDate.now();
            this.isAvailable = false;
            return true;
        }
        return false;
    }

    // Return the book
    public boolean returnBook() {
        if (!isAvailable) {
            this.borrower = null;
            this.borrowDate = null;
            this.isAvailable = true;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (isAvailable) {
            return String.format("'%s' by %s - AVAILABLE", title, author);
        } else {
            return String.format("'%s' by %s - BORROWED by %s on %s",
                    title, author, borrower, borrowDate.format(formatter));
        }
    }

    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        details.append(String.format("Title:  %s\n", title));
        details.append(String.format("Author: %s\n", author));
        details.append(String.format("Status: %s\n", isAvailable ? "Available" : "Borrowed"));
        if (!isAvailable) {
            details.append(String.format("Borrowed by: %s\n", borrower));
            details.append(String.format("Borrow date: %s\n",
                    borrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        }
        details.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        return details.toString();
    }
}

// Library Management System
class LibrarySystem {

    private ArrayList<Book> books;
    private LinkedList<String> transactionHistory;

    public LibrarySystem() {
        this.books = new ArrayList<>();
        this.transactionHistory = new LinkedList<>();
    }

    // Add a new book to the library
    public void addBook(String title, String author) {
        Book book = new Book(title, author);
        books.add(book);
        System.out.println("✓ Book added successfully!");
        System.out.println("  Title: " + title);
        System.out.println("  Author: " + author);
    }

    // Borrow a book
    public boolean borrowBook(String title, String userName) {
        // Check if username is provided
        if (userName == null || userName.trim().isEmpty()) {
            System.out.println("✗ Please provide your name to borrow a book.");
            return false;
        }

        // Find the book
        Book bookToBorrow = null;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                bookToBorrow = book;
                break;
            }
        }

        if (bookToBorrow == null) {
            System.out.println("✗ Book '" + title + "' is not available.");
            return false;
        }

        // Borrow the book
        if (bookToBorrow.borrowBook(userName)) {
            String transaction = LocalDate.now() + " - '" + title
                    + "' borrowed by " + userName;
            transactionHistory.addFirst(transaction);

            System.out.println("✓ Book borrowed successfully!");
            System.out.println("  Book: '" + title + "'");
            System.out.println("  Borrower: " + userName);
            System.out.println("  Date: " + LocalDate.now());
            return true;
        }

        return false;
    }

    // Return a book
    public boolean returnBook(String title, String userName) {
        // Check if username is provided
        if (userName == null || userName.trim().isEmpty()) {
            System.out.println("✗ Please provide your name to return a book.");
            return false;
        }

        // Find the book
        Book bookToReturn = null;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && !book.isAvailable()
                    && book.getBorrower().equalsIgnoreCase(userName)) {
                bookToReturn = book;
                break;
            }
        }

        if (bookToReturn == null) {
            System.out.println("✗ You haven't borrowed '" + title + "' or the book doesn't exist.");
            return false;
        }

        // Return the book
        if (bookToReturn.returnBook()) {
            String transaction = LocalDate.now() + " - '" + title
                    + "' returned by " + userName;
            transactionHistory.addFirst(transaction);

            System.out.println("✓ Book returned successfully!");
            System.out.println("  Book: '" + title + "'");
            System.out.println("  User: " + userName);
            return true;
        }

        return false;
    }

    // Search books by title or author
    public void searchBooks(String searchTerm) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  🔍 Search Results for '" + searchTerm + "'");
        System.out.println("╚════════════════════════════════════════════════╝");

        boolean found = false;
        int count = 1;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println(count + ". " + book);
                found = true;
                count++;
            }
        }

        if (!found) {
            System.out.println("No books found matching '" + searchTerm + "'");
        }
        System.out.println();
    }

    // Display all books
    public void displayAllBooks() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  📚 All Books in Library");
        System.out.println("╚════════════════════════════════════════════════╝");

        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
        System.out.println();
    }

    // Display available books
    public void displayAvailableBooks() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  ✓ Available Books");
        System.out.println("╚════════════════════════════════════════════════╝");

        boolean hasAvailable = false;
        int count = 1;
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println(count + ". " + book);
                hasAvailable = true;
                count++;
            }
        }

        if (!hasAvailable) {
            System.out.println("No books currently available.");
        }
        System.out.println();
    }

    // Display borrowed books
    public void displayBorrowedBooks() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  📖 Borrowed Books");
        System.out.println("╚════════════════════════════════════════════════╝");

        boolean hasBorrowed = false;
        int count = 1;
        for (Book book : books) {
            if (!book.isAvailable()) {
                System.out.println(count + ". " + book);
                hasBorrowed = true;
                count++;
            }
        }

        if (!hasBorrowed) {
            System.out.println("No books currently borrowed.");
        }
        System.out.println();
    }

    // Display books borrowed by a specific user
    public void displayUserBooks(String userName) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  📖 Books Borrowed by " + userName);
        System.out.println("╚════════════════════════════════════════════════╝");

        boolean found = false;
        int count = 1;
        for (Book book : books) {
            if (!book.isAvailable() && book.getBorrower().equalsIgnoreCase(userName)) {
                System.out.println(count + ". " + book);
                found = true;
                count++;
            }
        }

        if (!found) {
            System.out.println(userName + " hasn't borrowed any books.");
        }
        System.out.println();
    }

    // Display transaction history
    public void displayHistory(int limit) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  📜 Recent Transaction History");
        System.out.println("╚════════════════════════════════════════════════╝");

        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction history available.");
            return;
        }

        int count = 0;
        for (String transaction : transactionHistory) {
            if (count >= limit) {
                break;
            }
            System.out.println((count + 1) + ". " + transaction);
            count++;
        }
        System.out.println();
    }

    // Display library statistics
    public void displayStats() {
        int totalBooks = books.size();
        int availableBooks = 0;
        int borrowedBooks = 0;

        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks++;
            } else {
                borrowedBooks++;
            }
        }

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("  📊 Library Statistics");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("Total Books:        " + totalBooks);
        System.out.println("Available Books:    " + availableBooks);
        System.out.println("Borrowed Books:     " + borrowedBooks);
        System.out.println("Total Transactions: " + transactionHistory.size());
        System.out.println();
    }
}

// Main class
public class LibraryManagementSystem {

    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();
        Scanner scanner = new Scanner(System.in);

        // Add sample books
        System.out.println("\n--- Adding Sample Books ---");
        library.addBook("To Kill a Mockingbird", "Harper Lee");
        library.addBook("1984", "George Orwell");
        library.addBook("Pride and Prejudice", "Jane Austen");
        library.addBook("The Great Gatsby", "F. Scott Fitzgerald");
        library.addBook("Harry Potter and the Sorcerer's Stone", "J.K. Rowling");

        // Main menu loop
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║       📚 LIBRARY MANAGEMENT SYSTEM 📚          ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("1. Add Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Search Books");
            System.out.println("5. Display All Books");
            System.out.println("6. Display Available Books");
            System.out.println("7. Display Borrowed Books");
            System.out.println("8. Display My Borrowed Books");
            System.out.println("9. Display Transaction History");
            System.out.println("10. Display Library Statistics");
            System.out.println("0. Exit");
            System.out.println("════════════════════════════════════════════════");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("\nEnter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    library.addBook(title, author);
                    break;

                case 2:
                    System.out.print("\nEnter book title to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String borrower = scanner.nextLine();
                    library.borrowBook(borrowTitle, borrower);
                    break;

                case 3:
                    System.out.print("\nEnter book title to return: ");
                    String returnTitle = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String returner = scanner.nextLine();
                    library.returnBook(returnTitle, returner);
                    break;

                case 4:
                    System.out.print("\nEnter search term (title or author): ");
                    String searchTerm = scanner.nextLine();
                    library.searchBooks(searchTerm);
                    break;

                case 5:
                    library.displayAllBooks();
                    break;

                case 6:
                    library.displayAvailableBooks();
                    break;

                case 7:
                    library.displayBorrowedBooks();
                    break;

                case 8:
                    System.out.print("\nEnter your name: ");
                    String userName = scanner.nextLine();
                    library.displayUserBooks(userName);
                    break;

                case 9:
                    System.out.print("\nEnter number of recent transactions to display: ");
                    int limit = scanner.nextInt();
                    library.displayHistory(limit);
                    break;

                case 10:
                    library.displayStats();
                    break;

                case 0:
                    System.out.println("\n╔════════════════════════════════════════════════╗");
                    System.out.println("║   Thank you for using Library Management!     ║");
                    System.out.println("╚════════════════════════════════════════════════╝");
                    scanner.close();
                    return;

                default:
                    System.out.println("✗ Invalid choice. Please try again.");
            }
        }
    }
}
