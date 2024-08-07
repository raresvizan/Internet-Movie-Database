package org.example;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private List<Production> productions = IMDB.getInstance().productions;
    private List<User> users = IMDB.getInstance().users;
    private List<Actor> actors = IMDB.getInstance().actors;
    private List<Request> requests = IMDB.getInstance().requests;
    private User u;
    private JPanel productionsPanel = new JPanel();
    private JPanel favoritesPanel = new JPanel();
    private JPanel actorsPanel = new JPanel();
    private JPanel infoPanel = new JPanel();
    private String[] menuOptions;

    public GUI() {
        setTitle("IMDB - by Rares Visan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);
        JPanel panelLogin = loginPagePanel();
        cards.add(panelLogin, "login");
        add(cards);
        setLocationRelativeTo(null);
    }

    private JPanel loginPagePanel() {
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new BoxLayout(panelLogin, BoxLayout.Y_AXIS));
        panelLogin.setBackground(new Color(34, 34, 34));
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(34, 34, 34));
        JTextField email = new JTextField(25);
        email.setBorder(BorderFactory.createCompoundBorder(
                email.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JPasswordField password = new JPasswordField(20);
        password.setBorder(BorderFactory.createCompoundBorder(
                password.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JButton btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(80, 30));
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailText = email.getText();
                char[] passwordChars = password.getPassword();
                String passwordText = new String(passwordChars);
                boolean found = false;
                for (User user : users){
                    Credentials c = user.info.getCredentials();
                    if (c.getEmail().equals(emailText) && c.getPassword().equals(passwordText)){
                        found = true;
                        if(user instanceof Admin){
                            menuOptions = new String[]{"Add production", "Delete production", "Add actor","Delete actor",
                                    "Add user", "Delete user", "Update production", "Update actor", "Solve requests",
                                    "Logout"};
                        } else if (user instanceof Regular) {
                            menuOptions = new String[]{"Create request", "Delete request", "Delete rating","Logout"};
                        } else{
                            menuOptions = new String[]{"Add production", "Delete production", "Add actor","Delete actor",
                                    "Create request", "Delete request", "Update production", "Update actor",
                                    "Solve requests", "Logout"};
                        }
                        u = user;
                        JPanel panelMainPage = mainPagePanel();
                        cards.add(panelMainPage, "mainPage");
                        JPanel profilePage = profilePagePanel();
                        cards.add(profilePage, "profile");
                        JPanel actorsPage = actorsPagePanel();
                        cards.add(actorsPage, "actorsPage");
                        cardLayout.show(cards, "mainPage");
                    }
                }
                if (!found){
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                    email.setText("");
                    password.setText("");
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(password, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(btnLogin, gbc);

        panelLogin.add(formPanel, BorderLayout.CENTER);
        add(panelLogin);
        setLocationRelativeTo(null);
        return panelLogin;
    }
    private JPanel mainPagePanel() {
        JPanel panelMainPage = new JPanel();
        panelMainPage.setLayout(new BoxLayout(panelMainPage, BoxLayout.Y_AXIS));
        panelMainPage.setBackground(new Color(34, 34, 34));

        JPanel searchBarPanel = createSearchBar();
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelMainPage.add(searchBarPanel);

        JPanel welcomePanel = new JPanel(new FlowLayout());
        welcomePanel.setBackground(new Color(34, 34, 34));
        JLabel welcomeLabel = new JLabel("Welcome, " + u.username + "!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomePanel.add(welcomeLabel);
        panelMainPage.add(welcomePanel);

        JPanel btnsPanel = new JPanel(new FlowLayout());
        btnsPanel.setBackground(new Color(34, 34, 34));
        btnsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JButton btnActors = new JButton("Actors");
        btnActors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "actorsPage");
            }
        });
        btnsPanel.add(btnActors);
        JButton btnProfile = new JButton("Profile");
        btnProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "profile");
            }
        });
        btnsPanel.add(btnProfile);
        JButton btnMenu = new JButton("Menu");
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = (String) JOptionPane.showInputDialog(null, "What do you want to do?",
                        "Menu", JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);
                if(option!=null) {
                    doMenuOptions(option);
                }
            }
        });
        btnsPanel.add(btnMenu);
        panelMainPage.add(btnsPanel);

        JPanel productionsPanel = productionsPanel();
        productionsPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        panelMainPage.add(productionsPanel);

        setLocationRelativeTo(null);
        return panelMainPage;
    }

    private JPanel resultsPagePanel(String searchText){
        JPanel resultsPagePanel = new JPanel();
        resultsPagePanel.setLayout(new BoxLayout(resultsPagePanel, BoxLayout.Y_AXIS));
        resultsPagePanel.setBackground(new Color(34, 34, 34));

        JPanel searchBarPanel = createSearchBar();
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        resultsPagePanel.add(searchBarPanel);

        JPanel btnsPanel = new JPanel(new FlowLayout());
        btnsPanel.setBackground(new Color(34, 34, 34));
        btnsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        JButton btnBack = new JButton("Main Page");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "mainPage");
            }
        });
        btnsPanel.add(btnBack);
        JButton btnActors = new JButton("Actors");
        btnActors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "actorsPage");
            }
        });
        btnsPanel.add(btnActors);
        JButton btnProfile = new JButton("Profile");
        btnProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "profile");
            }
        });
        btnsPanel.add(btnProfile);
        JButton btnMenu = new JButton("Menu");
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = (String) JOptionPane.showInputDialog(null, "What do you want to do?",
                        "Menu", JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);
                if(option!=null) {
                    doMenuOptions(option);
                }
            }
        });
        btnsPanel.add(btnMenu);
        resultsPagePanel.add(btnsPanel);

        JLabel searchLabel = new JLabel("Results for \"" + searchText + "\":");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        searchLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        resultsPagePanel.add(searchLabel);

        JPanel resultsPanel = resultsPanel(searchText);
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        scrollPane.setBackground(new Color(34, 34, 34));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        resultsPagePanel.add(scrollPane);

        return resultsPagePanel;
    }

    private JPanel actorsPagePanel(){
        JPanel actorsPagePanel = new JPanel();
        actorsPagePanel.setLayout(new BoxLayout(actorsPagePanel, BoxLayout.Y_AXIS));
        actorsPagePanel.setBackground(new Color(34, 34, 34));

        JPanel searchBarPanel = createSearchBar();
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        actorsPagePanel.add(searchBarPanel);

        JPanel btnsPanel = new JPanel(new FlowLayout());
        btnsPanel.setBackground(new Color(34, 34, 34));
        btnsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        JButton btnBack = new JButton("Main Page");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards,"mainPage");
            }
        });
        btnsPanel.add(btnBack);
        JButton btnProfile = new JButton("Profile");
        btnProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "profile");
            }
        });
        btnsPanel.add(btnProfile);
        JButton btnMenu = new JButton("Menu");
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = (String) JOptionPane.showInputDialog(null, "What do you want to do?",
                        "Menu", JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);
                if(option!=null) {
                    doMenuOptions(option);
                }
            }
        });
        btnsPanel.add(btnMenu);
        actorsPagePanel.add(btnsPanel);

        JLabel actorsLabel = new JLabel("Actors:");
        actorsLabel.setForeground(Color.WHITE);
        actorsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        actorsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 100));
        actorsPagePanel.add(actorsLabel);

        makeActorsPanel();

        JScrollPane scrollPane = new JScrollPane(actorsPanel);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        scrollPane.setBackground(new Color(34, 34, 34));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        actorsPagePanel.add(scrollPane);

        return actorsPagePanel;
    }

    private JPanel profilePagePanel(){
        JPanel profilePagePanel = new JPanel();
        profilePagePanel.setLayout(new BoxLayout(profilePagePanel, BoxLayout.Y_AXIS));
        profilePagePanel.setBackground(new Color(34, 34, 34));

        JPanel searchBarPanel = createSearchBar();
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        profilePagePanel.add(searchBarPanel);

        JPanel btnsPanel = new JPanel(new FlowLayout());
        btnsPanel.setBackground(new Color(34, 34, 34));
        btnsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JButton btnBack = new JButton("Main Page");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards,"mainPage");
            }
        });
        btnsPanel.add(btnBack);
        JButton btnActors = new JButton("Actors");
        btnActors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "actorsPage");
            }
        });
        btnsPanel.add(btnActors);
        JButton btnMenu = new JButton("Menu");
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = (String) JOptionPane.showInputDialog(null, "What do you want to do?",
                        "Menu", JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);
                if(option!=null) {
                    doMenuOptions(option);
                }
            }
        });
        btnsPanel.add(btnMenu);
        profilePagePanel.add(btnsPanel);

        BufferedImage picture = null;
        try {
            picture = ImageIO.read(new File("img/profile.jpg"));
            Image dimg = picture.getScaledInstance(125, 125, Image.SCALE_SMOOTH);
            picture = new BufferedImage(125, 125, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = picture.createGraphics();
            g2d.drawImage(dimg, 0, 0, null);
            g2d.dispose();
        } catch (Exception e) {
            //idk
        }

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.setBackground(new Color(34, 34, 34));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel pictureLabel = new JLabel(new ImageIcon(picture));
        pictureLabel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        profilePanel.add(pictureLabel, BorderLayout.WEST);
        makeInfoPanel();
        profilePanel.add(infoPanel, BorderLayout.CENTER);
        profilePagePanel.add(profilePanel);

        JLabel favoritesLabel = new JLabel("Favorites:");
        favoritesLabel.setForeground(Color.WHITE);
        favoritesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        favoritesLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 100));
        profilePagePanel.add(favoritesLabel);

        makeFavoritesPanel();

        JScrollPane scrollPane = new JScrollPane(favoritesPanel);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setBackground(new Color(34, 34, 34));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        profilePagePanel.add(scrollPane);

        return profilePagePanel;
    }

    private JPanel createSearchBar() {
        JPanel searchBarPanel = new JPanel(new FlowLayout());
        searchBarPanel.setBackground(new Color(34, 34, 34));

        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("img/logo.png"));
            Image dimg = logo.getScaledInstance(150, 98, Image.SCALE_SMOOTH);
            logo = new BufferedImage(150, 98, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = logo.createGraphics();
            g2d.drawImage(dimg, 0, 0, null);
            g2d.dispose();
        } catch (Exception e) {
           //idk
        }
        JLabel logoLabel = new JLabel(new ImageIcon(logo));
        searchBarPanel.add(logoLabel, BorderLayout.WEST);

        JTextField searchBar = new JTextField(40);
        searchBar.setPreferredSize(new Dimension(500, 30));
        searchBar.setBorder(BorderFactory.createCompoundBorder(
                searchBar.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JButton btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(80, 30));
        searchBarPanel.add(searchBar);
        searchBarPanel.add(btnSearch);
        searchBar.setText("Search for a movie or actor...");
        searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (searchBar.getText().equals("Search for a movie or actor...")) {
                    searchBar.setText("");
                }
            }
        });
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchBar.getText().equals("Search for a movie or actor...")) {
                    searchBar.setText("");
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText();
                if (!searchText.equals("Search for a movie or actor...") && !searchText.isEmpty()) {
                    searchBar.setText("Search for a movie or actor...");
                    JPanel resultsPage = resultsPagePanel(searchText);
                    cards.add(resultsPage, "resultsPage");
                    cardLayout.show(cards, "resultsPage");
                }
            }
        });

        return searchBarPanel;
    }

    private JPanel productionsPanel() {
        JPanel recommendedPanel = new JPanel(new BorderLayout());
        recommendedPanel.setBackground(new Color(34, 34, 34));

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(34, 34, 34));
        JLabel recommendedLabel = new JLabel("Recommended for you");
        recommendedLabel.setForeground(Color.WHITE);
        recommendedLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        recommendedLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 200));
        buttonsPanel.add(recommendedLabel);
        JButton btnButton1 = new JButton("Sort by rating");
        btnButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Highest rating", "Lowest rating"};
                int option = JOptionPane.showOptionDialog(null, "Sort by rating", "Sort by rating",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (option == 0) {
                    productions.sort((p1, p2) -> p2.compareByRating(p1));
                } else {
                    productions.sort(Production::compareByRating);
                }
                // refresh panel
                productionsPanel.removeAll();
                makeProdList(null);
                productionsPanel.revalidate();
                productionsPanel.repaint();
            }
        });
        JButton btnButton2 = new JButton("Sort by genre");
        btnButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] genres = Arrays.stream(Genre.values()).map(Enum::name).toArray(String[]::new);
                String genre = (String) JOptionPane.showInputDialog(null, "Sort by genre", "Sort by genre",
                        JOptionPane.PLAIN_MESSAGE, null, genres, genres[0]);
                // refresh panel
                productionsPanel.removeAll();
                makeProdList(genre);
                productionsPanel.revalidate();
                productionsPanel.repaint();
            }
        });
        buttonsPanel.add(btnButton1);
        buttonsPanel.add(btnButton2);
        recommendedPanel.add(buttonsPanel, BorderLayout.NORTH);

        productionsPanel.setLayout(new BoxLayout(productionsPanel, BoxLayout.Y_AXIS));
        productionsPanel.setBackground(new Color(34, 34, 34));
        makeProdList(null);

        JScrollPane scrollPane = new JScrollPane(productionsPanel);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBackground(new Color(34, 34, 34));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        recommendedPanel.add(scrollPane);

        return recommendedPanel;
    }

    private void makeFavoritesPanel(){
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        favoritesPanel.setBackground(new Color(34, 34, 34));

        JLabel actorsLabel = new JLabel("Actors:");
        actorsLabel.setForeground(Color.WHITE);
        actorsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        favoritesPanel.add(actorsLabel);
        boolean found = false;
        for(Object o : u.preferences){
            if(o instanceof Actor a){
                JPanel actorPanel = new JPanel();
                actorPanel.setBackground(new Color(34, 34, 34));
                actorPanel.setLayout(new BorderLayout());
                actorPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                JLabel actorNameLabel = new JLabel(a.name);
                actorNameLabel.setForeground(Color.WHITE);
                actorNameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                actorNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                JLabel actorPhotoLabel = new JLabel(new ImageIcon(image));
                actorPanel.add(actorPhotoLabel, BorderLayout.WEST);
                actorPanel.add(actorNameLabel, BorderLayout.CENTER);
                JButton btnRemove = new JButton("Remove");
                btnRemove.setBackground(new Color(220,20,60));
                btnRemove.setForeground(Color.BLACK);
                btnRemove.setFont(new Font("Arial", Font.BOLD, 15));
                btnRemove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        u.removePreference(a);
                        favoritesPanel.removeAll();
                        makeFavoritesPanel();
                        favoritesPanel.revalidate();
                        favoritesPanel.repaint();
                        actorsPanel.removeAll();
                        makeActorsPanel();
                        actorsPanel.revalidate();
                        actorsPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Actor removed from favorites!");
                    }
                });
                actorPanel.add(btnRemove, BorderLayout.EAST);
                favoritesPanel.add(actorPanel);
                found = true;

                actorPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showActorDetails(a);
                    }
                });
            }
        }
        if(!found){
            JLabel noActorsLabel = new JLabel("No actors found");
            noActorsLabel.setForeground(Color.WHITE);
            noActorsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            favoritesPanel.add(noActorsLabel);
        }
        JLabel moviesLabel = new JLabel("Movies:");
        moviesLabel.setForeground(Color.WHITE);
        moviesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        moviesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        favoritesPanel.add(moviesLabel);
        found = false;
        for(Object o : u.preferences){
            if(o instanceof Movie m){
                JPanel moviePanel = new JPanel();
                moviePanel.setBackground(new Color(34, 34, 34));
                moviePanel.setLayout(new BorderLayout());
                moviePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                JLabel titleLabel = new JLabel(m.title);
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                JLabel ratingLabel = new JLabel("Rating: " + m.totalRating + " / 10");
                ratingLabel.setForeground(Color.WHITE);
                ratingLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                JLabel moviePhotoLabel = new JLabel(new ImageIcon(image));
                moviePanel.add(moviePhotoLabel, BorderLayout.WEST);
                JPanel titleRatingPanel = new JPanel();
                titleRatingPanel.setLayout(new BoxLayout(titleRatingPanel, BoxLayout.Y_AXIS));
                titleRatingPanel.setBackground(new Color(34, 34, 34));
                titleRatingPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 0));
                titleRatingPanel.add(titleLabel);
                titleRatingPanel.add(ratingLabel, BorderLayout.SOUTH);
                moviePanel.add(titleRatingPanel, BorderLayout.CENTER);
                JButton btnRemove = new JButton("Remove");
                btnRemove.setBackground(new Color(220,20,60));
                btnRemove.setForeground(Color.BLACK);
                btnRemove.setFont(new Font("Arial", Font.BOLD, 15));
                btnRemove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        u.removePreference(m);
                        favoritesPanel.removeAll();
                        makeFavoritesPanel();
                        favoritesPanel.revalidate();
                        favoritesPanel.repaint();
                        productionsPanel.removeAll();
                        makeProdList(null);
                        productionsPanel.revalidate();
                        productionsPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Movie removed from favorites!");
                    }
                });
                moviePanel.add(btnRemove, BorderLayout.EAST);
                favoritesPanel.add(moviePanel);
                found = true;

                moviePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showProdDetails(m);
                    }
                });
            }
        }
        if(!found){
            JLabel noMoviesLabel = new JLabel("No movies found");
            noMoviesLabel.setForeground(Color.WHITE);
            noMoviesLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            favoritesPanel.add(noMoviesLabel);
        }
        JLabel seriesLabel = new JLabel("Series:");
        seriesLabel.setForeground(Color.WHITE);
        seriesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        seriesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        favoritesPanel.add(seriesLabel);
        found = false;
        for(Object o : u.preferences){
            if(o instanceof Series s){
                JPanel seriesPanel = new JPanel();
                seriesPanel.setBackground(new Color(34, 34, 34));
                seriesPanel.setLayout(new BorderLayout());
                seriesPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                JLabel titleLabel = new JLabel(s.title);
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                JLabel ratingLabel = new JLabel("Rating: " + s.totalRating + " / 10");
                ratingLabel.setForeground(Color.WHITE);
                ratingLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                JLabel seriesPhotoLabel = new JLabel(new ImageIcon(image));
                seriesPanel.add(seriesPhotoLabel, BorderLayout.WEST);
                JPanel titleRatingPanel = new JPanel();
                titleRatingPanel.setLayout(new BoxLayout(titleRatingPanel, BoxLayout.Y_AXIS));
                titleRatingPanel.setBackground(new Color(34, 34, 34));
                titleRatingPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 0));
                titleRatingPanel.add(titleLabel);
                titleRatingPanel.add(ratingLabel, BorderLayout.SOUTH);
                seriesPanel.add(titleRatingPanel, BorderLayout.CENTER);
                JButton btnRemove = new JButton("Remove");
                btnRemove.setBackground(new Color(220,20,60));
                btnRemove.setForeground(Color.BLACK);
                btnRemove.setFont(new Font("Arial", Font.BOLD, 15));
                btnRemove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        u.removePreference(s);
                        favoritesPanel.removeAll();
                        makeFavoritesPanel();
                        favoritesPanel.revalidate();
                        favoritesPanel.repaint();
                        productionsPanel.removeAll();
                        makeProdList(null);
                        productionsPanel.revalidate();
                        productionsPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Series removed from favorites!");
                    }
                });
                seriesPanel.add(btnRemove, BorderLayout.EAST);
                favoritesPanel.add(seriesPanel);
                found = true;

                seriesPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showProdDetails(s);
                    }
                });
            }
        }
        if(!found){
            JLabel noSeriesLabel = new JLabel("No series found");
            noSeriesLabel.setForeground(Color.WHITE);
            noSeriesLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            favoritesPanel.add(noSeriesLabel);
        }
    }
    private JPanel resultsPanel(String searchText) {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(new Color(34, 34, 34));

        JLabel actorsLabel = new JLabel("Actors:");
        actorsLabel.setForeground(Color.WHITE);
        actorsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        resultsPanel.add(actorsLabel);
        boolean found = false;
        for(Actor a : actors){
            if(a.name.toLowerCase().startsWith(searchText.toLowerCase())){
                JPanel actorPanel = new JPanel();
                actorPanel.setBackground(new Color(34, 34, 34));
                actorPanel.setLayout(new BorderLayout());
                actorPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                JLabel nameLabel = new JLabel(a.name);
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                JLabel pictureLabel = new JLabel(new ImageIcon(image));
                actorPanel.add(pictureLabel, BorderLayout.WEST);
                actorPanel.add(nameLabel, BorderLayout.CENTER);
                resultsPanel.add(actorPanel);
                found = true;
                JButton btnAdd = new JButton("Add to favorites");
                btnAdd.setBackground(new Color(50, 205, 50));
                btnAdd.setForeground(Color.BLACK);
                btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
                btnAdd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        u.addPreference(a);
                        favoritesPanel.removeAll();
                        makeFavoritesPanel();
                        favoritesPanel.revalidate();
                        favoritesPanel.repaint();
                        actorsPanel.removeAll();
                        makeActorsPanel();
                        actorsPanel.revalidate();
                        actorsPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Actor added to favorites!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(cards, "mainPage");
                    }
                });
                if(!u.preferences.contains(a)){
                    actorPanel.add(btnAdd, BorderLayout.EAST);
                }
                actorPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showActorDetails(a);
                    }
                });
            }
        }
        if(!found){
            JLabel noActorsLabel = new JLabel("No actors found");
            noActorsLabel.setForeground(Color.WHITE);
            noActorsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            resultsPanel.add(noActorsLabel);
        }
        JLabel moviesLabel = new JLabel("Movies:");
        moviesLabel.setForeground(Color.WHITE);
        moviesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        moviesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        resultsPanel.add(moviesLabel);
        found = false;
        for(Production p : productions){
            if(p instanceof Movie m){
                if(m.title.toLowerCase().startsWith(searchText.toLowerCase())){
                    JPanel moviePanel = new JPanel();
                    moviePanel.setBackground(new Color(34, 34, 34));
                    moviePanel.setLayout(new BorderLayout());
                    moviePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    JLabel titleLabel = new JLabel(m.title);
                    titleLabel.setForeground(Color.WHITE);
                    titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    JLabel ratingLabel = new JLabel("Rating: " + m.totalRating + " / 10");
                    ratingLabel.setForeground(Color.WHITE);
                    ratingLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                    JLabel pictureLabel = new JLabel(new ImageIcon(image));
                    moviePanel.add(pictureLabel, BorderLayout.WEST);
                    JPanel titleRatingPanel = new JPanel();
                    titleRatingPanel.setLayout(new BoxLayout(titleRatingPanel, BoxLayout.Y_AXIS));
                    titleRatingPanel.setBackground(new Color(34, 34, 34));
                    titleRatingPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 0));
                    titleRatingPanel.add(titleLabel);
                    titleRatingPanel.add(ratingLabel, BorderLayout.SOUTH);
                    moviePanel.add(titleRatingPanel, BorderLayout.CENTER);
                    JPanel buttonsPanel = new JPanel(new BorderLayout());
                    buttonsPanel.setBackground(new Color(34, 34, 34));
                    JButton btnRate = new JButton("Rate");
                    btnRate.setBackground(new Color(255, 195, 0));
                    btnRate.setForeground(Color.BLACK);
                    btnRate.setFont(new Font("Arial", Font.BOLD, 15));
                    btnRate.setPreferredSize(new Dimension(130, 50));
                    btnRate.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String[] options = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                            try {
                                Long rate = Long.parseLong((String) JOptionPane.showInputDialog(null, "Rate", "Rate",
                                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
                                String comment = JOptionPane.showInputDialog(null, "Comment", "Comment",
                                        JOptionPane.PLAIN_MESSAGE);
                                Rating rating = new Rating(u.username, rate, comment);
                                p.ratings.add(rating);
                                ((Regular) u).addRating();
                                p.calculateRating();
                                productionsPanel.removeAll();
                                makeProdList(null);
                                productionsPanel.revalidate();
                                productionsPanel.repaint();
                                infoPanel.removeAll();
                                makeInfoPanel();
                                infoPanel.revalidate();
                                infoPanel.repaint();
                                JOptionPane.showMessageDialog(null, "Production rated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                                cardLayout.show(cards, "mainPage");
                            } catch (Exception ex) {
                                //idk
                            }
                        }
                    });
                    if(u instanceof Regular){
                        boolean rated = false;
                        for(Rating r : p.ratings){
                            if(r.user.equals(u.username)){
                                rated = true;
                                break;
                            }
                        }
                        if(!rated){
                            buttonsPanel.add(btnRate, BorderLayout.NORTH);
                        }
                    }
                    JButton btnAdd = new JButton("Add to favorites");
                    btnAdd.setBackground(new Color(50, 205, 50));
                    btnAdd.setForeground(Color.BLACK);
                    btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
                    btnAdd.setPreferredSize(new Dimension(130, 50));
                    btnAdd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            u.addPreference(p);
                            favoritesPanel.removeAll();
                            makeFavoritesPanel();
                            favoritesPanel.revalidate();
                            favoritesPanel.repaint();
                            productionsPanel.removeAll();
                            makeProdList(null);
                            productionsPanel.revalidate();
                            productionsPanel.repaint();
                            JOptionPane.showMessageDialog(null, "Production added to favorites!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            cardLayout.show(cards, "mainPage");
                        }
                    });
                    if(!u.preferences.contains(p)){
                        buttonsPanel.add(btnAdd, BorderLayout.SOUTH);
                    }
                    moviePanel.add(buttonsPanel, BorderLayout.EAST);
                    resultsPanel.add(moviePanel);
                    found = true;

                    moviePanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            showProdDetails(p);
                        }
                    });
                }
            }
        }
        if(!found){
            JLabel noMoviesLabel = new JLabel("No movies found");
            noMoviesLabel.setForeground(Color.WHITE);
            noMoviesLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            resultsPanel.add(noMoviesLabel);
        }
        JLabel seriesLabel = new JLabel("Series:");
        seriesLabel.setForeground(Color.WHITE);
        seriesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        seriesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        resultsPanel.add(seriesLabel);
        found = false;
        for(Production p : productions){
            if(p instanceof Series s){
                if(s.title.toLowerCase().startsWith(searchText.toLowerCase())){
                    JPanel seriesPanel = new JPanel();
                    seriesPanel.setBackground(new Color(34, 34, 34));
                    seriesPanel.setLayout(new BorderLayout());
                    seriesPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    JLabel titleLabel = new JLabel(s.title);
                    titleLabel.setForeground(Color.WHITE);
                    titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    JLabel ratingLabel = new JLabel("Rating: " + s.totalRating + " / 10");
                    ratingLabel.setForeground(Color.WHITE);
                    ratingLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                    JLabel pictureLabel = new JLabel(new ImageIcon(image));
                    seriesPanel.add(pictureLabel, BorderLayout.WEST);
                    JPanel titleRatingPanel = new JPanel();
                    titleRatingPanel.setLayout(new BoxLayout(titleRatingPanel, BoxLayout.Y_AXIS));
                    titleRatingPanel.setBackground(new Color(34, 34, 34));
                    titleRatingPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 0));
                    titleRatingPanel.add(titleLabel);
                    titleRatingPanel.add(ratingLabel, BorderLayout.SOUTH);
                    seriesPanel.add(titleRatingPanel, BorderLayout.CENTER);
                    JPanel buttonsPanel = new JPanel(new BorderLayout());
                    buttonsPanel.setBackground(new Color(34, 34, 34));
                    JButton btnRate = new JButton("Rate");
                    btnRate.setBackground(new Color(255, 195, 0));
                    btnRate.setForeground(Color.BLACK);
                    btnRate.setFont(new Font("Arial", Font.BOLD, 15));
                    btnRate.setPreferredSize(new Dimension(130, 50));
                    btnRate.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String[] options = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                            try {
                                Long rate = Long.parseLong((String) JOptionPane.showInputDialog(null, "Rate", "Rate",
                                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
                                String comment = JOptionPane.showInputDialog(null, "Comment", "Comment",
                                        JOptionPane.PLAIN_MESSAGE);
                                Rating rating = new Rating(u.username, rate, comment);
                                p.ratings.add(rating);
                                ((Regular) u).addRating();
                                p.calculateRating();
                                productionsPanel.removeAll();
                                makeProdList(null);
                                productionsPanel.revalidate();
                                productionsPanel.repaint();
                                infoPanel.removeAll();
                                makeInfoPanel();
                                infoPanel.revalidate();
                                infoPanel.repaint();
                                JOptionPane.showMessageDialog(null, "Production rated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                                cardLayout.show(cards, "mainPage");
                            } catch (Exception ex) {
                                //idk
                            }
                        }
                    });
                    if(u instanceof Regular){
                        boolean rated = false;
                        for(Rating r : p.ratings){
                            if(r.user.equals(u.username)){
                                rated = true;
                                break;
                            }
                        }
                        if(!rated){
                            buttonsPanel.add(btnRate, BorderLayout.NORTH);
                        }
                    }
                    JButton btnAdd = new JButton("Add to favorites");
                    btnAdd.setBackground(new Color(50, 205, 50));
                    btnAdd.setForeground(Color.BLACK);
                    btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
                    btnAdd.setPreferredSize(new Dimension(130, 50));
                    btnAdd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            u.addPreference(p);
                            favoritesPanel.removeAll();
                            makeFavoritesPanel();
                            favoritesPanel.revalidate();
                            favoritesPanel.repaint();
                            productionsPanel.removeAll();
                            makeProdList(null);
                            productionsPanel.revalidate();
                            productionsPanel.repaint();
                            JOptionPane.showMessageDialog(null, "Production added to favorites!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            cardLayout.show(cards, "mainPage");
                        }
                    });
                    if(!u.preferences.contains(p)){
                        buttonsPanel.add(btnAdd, BorderLayout.SOUTH);
                    }
                    seriesPanel.add(buttonsPanel, BorderLayout.EAST);
                    resultsPanel.add(seriesPanel);
                    found = true;

                    seriesPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            showProdDetails(p);
                        }
                    });
                }
            }
        }
        if(!found){
            JLabel noSeriesLabel = new JLabel("No series found");
            noSeriesLabel.setForeground(Color.WHITE);
            noSeriesLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            resultsPanel.add(noSeriesLabel);
        }

        return resultsPanel;
    }

    private void makeProdList(String genre) {
        for (Production p : productions) {
            if(genre == null || p.genres.contains(Genre.valueOf(genre))) {
                JPanel prod = new JPanel();
                prod.setBackground(new Color(34, 34, 34));
                prod.setLayout(new BorderLayout());
                prod.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                JLabel titleLabel = new JLabel(p.title);
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                JLabel ratingLabel = new JLabel("Rating: " + p.totalRating + " / 10");
                ratingLabel.setForeground(Color.WHITE);
                BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                JLabel pictureLabel = new JLabel(new ImageIcon(image));
                prod.add(pictureLabel, BorderLayout.WEST);
                JPanel titleRatingPanel = new JPanel();
                titleRatingPanel.setLayout(new BoxLayout(titleRatingPanel, BoxLayout.Y_AXIS));
                titleRatingPanel.setBackground(new Color(34, 34, 34));
                titleRatingPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 0));
                titleRatingPanel.add(titleLabel);
                titleRatingPanel.add(ratingLabel, BorderLayout.SOUTH);
                prod.add(titleRatingPanel, BorderLayout.CENTER);

                JPanel buttonsPanel = new JPanel(new BorderLayout());
                buttonsPanel.setBackground(new Color(34, 34, 34));
                JButton btnRate = new JButton("Rate");
                btnRate.setBackground(new Color(255, 195, 0));
                btnRate.setForeground(Color.BLACK);
                btnRate.setFont(new Font("Arial", Font.BOLD, 15));
                btnRate.setPreferredSize(new Dimension(130, 50));
                btnRate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] options = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                        try {
                            Long rate = Long.parseLong((String) JOptionPane.showInputDialog(null, "Rate", "Rate",
                                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
                            String comment = JOptionPane.showInputDialog(null, "Comment", "Comment",
                                    JOptionPane.PLAIN_MESSAGE);
                            Rating rating = new Rating(u.username, rate, comment);
                            p.ratings.add(rating);
                            ((Regular) u).addRating();
                            p.calculateRating();
                            productionsPanel.removeAll();
                            makeProdList(null);
                            productionsPanel.revalidate();
                            productionsPanel.repaint();
                            infoPanel.removeAll();
                            makeInfoPanel();
                            infoPanel.revalidate();
                            infoPanel.repaint();
                            JOptionPane.showMessageDialog(null, "Production rated!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            IMDB.getInstance().notificationReviewRegular(p, u.username);
                            IMDB.getInstance().notificationReviewStaff(p);
                        } catch (Exception ex) {
                            //idk
                        }
                    }
                });
                if(u instanceof Regular){
                    boolean rated = false;
                    for(Rating r : p.ratings){
                        if(r.user.equals(u.username)){
                            rated = true;
                            break;
                        }
                    }
                    if(!rated){
                        buttonsPanel.add(btnRate, BorderLayout.NORTH);
                    }
                }
                JButton btnAdd = new JButton("Add to favorites");
                btnAdd.setBackground(new Color(50, 205, 50));
                btnAdd.setForeground(Color.BLACK);
                btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
                btnAdd.setPreferredSize(new Dimension(130, 50));
                btnAdd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        u.addPreference(p);
                        favoritesPanel.removeAll();
                        makeFavoritesPanel();
                        favoritesPanel.revalidate();
                        favoritesPanel.repaint();
                        productionsPanel.removeAll();
                        makeProdList(null);
                        productionsPanel.revalidate();
                        productionsPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Production added to favorites!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                if(!u.preferences.contains(p)){
                    buttonsPanel.add(btnAdd, BorderLayout.SOUTH);
                }
                prod.add(buttonsPanel, BorderLayout.EAST);
                prod.addMouseListener(new MouseAdapter() {
                          @Override
                    public void mouseClicked(MouseEvent e) {
                        showProdDetails(p);
                    }
                });
                productionsPanel.add(prod);
            }
        }
    }

    private void makeActorsPanel(){
        actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.Y_AXIS));
        actorsPanel.setBackground(new Color(34, 34, 34));
        actors.sort(Actor::compareTo);
        for(Actor a : actors){
            JPanel actorPanel = new JPanel();
            actorPanel.setBackground(new Color(34, 34, 34));
            actorPanel.setLayout(new BorderLayout());
            actorPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            JLabel nameLabel = new JLabel(a.name);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            JLabel pictureLabel = new JLabel(new ImageIcon(image));
            actorPanel.add(pictureLabel, BorderLayout.WEST);
            actorPanel.add(nameLabel, BorderLayout.CENTER);
            actorsPanel.add(actorPanel);

            JButton btnAdd = new JButton("Add to favorites");
            btnAdd.setBackground(new Color(50, 205, 50));
            btnAdd.setForeground(Color.BLACK);
            btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    u.addPreference(a);
                    favoritesPanel.removeAll();
                    makeFavoritesPanel();
                    favoritesPanel.revalidate();
                    favoritesPanel.repaint();
                    actorsPanel.removeAll();
                    makeActorsPanel();
                    actorsPanel.revalidate();
                    actorsPanel.repaint();
                    JOptionPane.showMessageDialog(null, "Actor added to favorites!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            if(!u.preferences.contains(a)){
                actorPanel.add(btnAdd, BorderLayout.EAST);
            }

            actorPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showActorDetails(a);
                }
            });
        }
    }

    private void makeInfoPanel(){
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(34, 34, 34));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        JLabel nameLabel = new JLabel(u.info.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        infoPanel.add(nameLabel);
        JLabel usernameLabel = new JLabel("@ " + u.username);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.add(usernameLabel);
        if(!(u instanceof Admin)){
            JLabel xpLabel = new JLabel("Experience: " + u.xp);
            xpLabel.setForeground(Color.WHITE);
            xpLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            infoPanel.add(xpLabel);
        }
        JLabel typeLabel = new JLabel("Role: " + u.type);
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.add(typeLabel);
        JButton btnNotifications = new JButton("Notifications");
        btnNotifications.setBackground(new Color(255, 195, 0));
        btnNotifications.setForeground(Color.BLACK);
        btnNotifications.setFont(new Font("Arial", Font.BOLD, 15));
        btnNotifications.setPreferredSize(new Dimension(100, 30));
        btnNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel notificationsPanel = new JPanel();
                notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));
                notificationsPanel.setBackground(new Color(34, 34, 34));
                notificationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                JLabel notificationsLabel = new JLabel("Your notifications:");
                notificationsLabel.setForeground(Color.WHITE);
                notificationsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                notificationsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
                notificationsPanel.add(notificationsLabel);
                if (u.notifications.isEmpty()) {
                    JLabel noNotificationsLabel = new JLabel("No notifications");
                    noNotificationsLabel.setForeground(Color.WHITE);
                    noNotificationsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    notificationsPanel.add(noNotificationsLabel);
                } else {
                    for (String s : u.notifications) {
                        JLabel notificationLabel = new JLabel(s);
                        notificationLabel.setForeground(Color.WHITE);
                        notificationLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                        notificationLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                        notificationsPanel.add(notificationLabel);
                    }
                }
                if(notificationsPanel.getPreferredSize().height > 500 || notificationsPanel.getPreferredSize().width > 700){
                    JScrollPane scrollPane = new JScrollPane(notificationsPanel);
                    scrollPane.setPreferredSize(new Dimension(700, 500));
                    scrollPane.setBackground(new Color(34, 34, 34));
                    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
                    scrollPane.getVerticalScrollBar().setUnitIncrement(10);

                    JOptionPane.showMessageDialog(null, scrollPane, "Notifications",
                            JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, notificationsPanel, "Notifications",
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        infoPanel.add(btnNotifications);
    }

    private void doMenuOptions(String option){
        switch (option){
            case "Add production" -> {
                addProductionOption();
            }
            case "Delete production" -> {
                deleteProductionOption();
            }
            case "Add actor" -> {
                addActorOption();
            }
            case "Delete actor" -> {
                deleteActorOption();
            }
            case "Add user" -> {
                addUserOption();
            }
            case "Delete user" -> {
                deleteUserOption();
            }
            case "Create request" -> {
                createRequestOption();
            }
            case "Delete request" -> {
                deleteRequestOption();
            }
            case "Delete rating" -> {
                deleteRatingOption();
            }
            case "Update production" -> {
                updateProductionOption();
            }
            case "Update actor" -> {
                updateActorOption();
            }
            case "Solve requests" -> {
                solveRequestsOption();
            }
            case "Logout" -> {
                dispose();
                run();
            }
        }
    }

    private void updateActorOption() {
        StringBuilder actorsNames = new StringBuilder();
        for(Object o : ((Staff) u).contributions){
            if(o instanceof Actor a) {
                actorsNames.append(a.name).append(", ");
            }
        }
        if(!actorsNames.isEmpty()) {
            String[] actorOptions = actorsNames.toString().split(", ");
            String option = (String) JOptionPane.showInputDialog(null, "Choose actor",
                    "Update actor", JOptionPane.PLAIN_MESSAGE, null, actorOptions, actorOptions[0]);
            if(option != null){
                Actor actor = null;
                for(Object o : ((Staff) u).contributions){
                    if(o instanceof Actor a) {
                        if(a.name.equals(option)) {
                            actor = a;
                            break;
                        }
                    }
                }
                String[] options = {"Name", "Biography", "Projects"};
                int option2 = JOptionPane.showOptionDialog(null, "Update actor", "Update actor",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if(option2 == -1){
                    return;
                }
                if(option2 == 0){
                    String newName = JOptionPane.showInputDialog(null, "New name", "Update actor",
                            JOptionPane.PLAIN_MESSAGE);
                    if(newName != null){
                        actor.name = newName;
                        actorsPanel.removeAll();
                        makeActorsPanel();
                        actorsPanel.revalidate();
                        actorsPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Actor updated!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                    }
                } else if(option2 == 1){
                    String newBio = JOptionPane.showInputDialog(null, "New biography", "Update actor",
                            JOptionPane.PLAIN_MESSAGE);
                    if(newBio != null){
                        actor.biography = newBio;
                        JOptionPane.showMessageDialog(null, "Actor updated!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if(option2 == 2){
                    String newProjects= JOptionPane.showInputDialog(null, "New project", "Update actor",
                            JOptionPane.PLAIN_MESSAGE);
                    if(newProjects != null){
                        try {
                            for (String s : newProjects.split(", ")) {
                                String[] split = s.split(" - ");
                                actor.projects.put(split[0], split[1]);
                            }
                            JOptionPane.showMessageDialog(null, "Actor updated!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Invalid episodes! Please try again.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }

    private void updateProductionOption() {
        StringBuilder productionsTitles = new StringBuilder();
        for(Object o : ((Staff) u).contributions){
            if(o instanceof Production p) {
                productionsTitles.append(p.title).append(" , ");
            }
        }
        if(!productionsTitles.isEmpty()) {
            String[] prodOptions = productionsTitles.toString().split(" , ");
            String option = (String) JOptionPane.showInputDialog(null, "Choose production",
                    "Update production", JOptionPane.PLAIN_MESSAGE, null, prodOptions, prodOptions[0]);
            if (option != null) {
                Production production = null;
                for (Production p : productions) {
                    if (p.title.equals(option)) {
                        production = p;
                        break;
                    }
                }
                String[] options = null;
                if(production instanceof Movie) {
                    options = new String[]{"Title", "Subject", "Genres", "Actors", "Directors", "Release year", "Duration"};
                } else if(production instanceof Series) {
                    options = new String[]{"Title", "Subject", "Genres", "Actors", "Directors", "Release year", "Number of seasons",
                    "Seasons and episodes"};
                }
                String option2 = (String) JOptionPane.showInputDialog(null, "Choose field",
                        "Update production", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if(option2 == null){
                    return;
                }
                switch (option2){
                    case "Title" -> {
                        String newTitle = JOptionPane.showInputDialog(null, "New title", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newTitle != null){
                            production.title = newTitle;
                            productionsPanel.removeAll();
                            makeProdList(null);
                            productionsPanel.revalidate();
                            productionsPanel.repaint();
                            JOptionPane.showMessageDialog(null, "Production updated!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    case "Subject" -> {
                        String newSubject = JOptionPane.showInputDialog(null, "New subject", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newSubject != null){
                            production.subject = newSubject;
                            JOptionPane.showMessageDialog(null, "Production updated!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    case "Genres" -> {
                        String newGenres = JOptionPane.showInputDialog(null, "New genres", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newGenres != null){
                            try {
                                for (String s : newGenres.split(", ")) {
                                    production.genres.add(Genre.valueOf(s.toUpperCase()));
                                }
                                JOptionPane.showMessageDialog(null, "Production updated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Invalid genres! Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    case "Actors" -> {
                        String newActors = JOptionPane.showInputDialog(null, "New actors", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newActors != null){
                            try {
                                for (String s : newActors.split(", ")) {
                                    production.actors.add(s);
                                }
                                JOptionPane.showMessageDialog(null, "Production updated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Invalid actors! Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    case "Directors" -> {
                        String newDirectors = JOptionPane.showInputDialog(null, "New directors", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newDirectors != null){
                            try {
                                for (String s : newDirectors.split(", ")) {
                                    production.directors.add(s);
                                }
                                JOptionPane.showMessageDialog(null, "Production updated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Invalid directors! Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    case "Duration" -> {
                        String newDuration = JOptionPane.showInputDialog(null, "New duration", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newDuration != null){
                            ((Movie) production).duration = newDuration;
                            JOptionPane.showMessageDialog(null, "Production updated!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    case "Release year" -> {
                        String newReleaseYear = JOptionPane.showInputDialog(null, "New release year", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newReleaseYear != null){
                            try {
                                if(production instanceof Movie){
                                    ((Movie) production).releaseYear = Long.parseLong(newReleaseYear);
                                } else if(production instanceof Series){
                                    ((Series) production).releaseYear = Long.parseLong(newReleaseYear);
                                }
                                JOptionPane.showMessageDialog(null, "Production updated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Invalid release year! Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    case "Number of seasons" -> {
                        String newNumberOfSeasons = JOptionPane.showInputDialog(null, "New number of seasons", "Update production",
                                JOptionPane.PLAIN_MESSAGE);
                        if(newNumberOfSeasons != null){
                            try {
                                ((Series) production).numberOfSeasons = Long.parseLong(newNumberOfSeasons);
                                JOptionPane.showMessageDialog(null, "Production updated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Invalid number of seasons! Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    case "Seasons and episodes" -> {
                        long nr = ((Series) production).numberOfSeasons;
                        while(((Series) production).getSeasonsMap().size()<nr) {
                            JTextField seasonField = new JTextField();
                            JTextField episodesField = new JTextField();
                            Object[] fields = {
                                    "Season:", seasonField,
                                    "Episodes:", episodesField
                            };
                            while(true) {
                                int result = JOptionPane.showConfirmDialog(null, fields, "Add season",
                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                if (result == JOptionPane.OK_OPTION) {
                                    if (seasonField.getText().isEmpty() || episodesField.getText().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                    } else{
                                        break;
                                    }
                                } else{
                                    break;
                                }
                            }
                            List<Episode> episodes = new ArrayList<>();
                            try {
                                for (String s : episodesField.getText().split(", ")) {
                                    String[] split = s.split(" - ");
                                    episodes.add(new Episode(split[0], split[1]));
                                }
                                ((Series) production).getSeasonsMap().put(seasonField.getText(), episodes);
                                JOptionPane.showMessageDialog(null, "Production updated!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Invalid episodes! Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            ((Series) production).getSeasonsMap().put(seasonField.getText(), episodes);
                        }
                    }
                }
            }
        }
    }

    private void deleteRatingOption() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(34, 34, 34));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Your ratings:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        detailsPanel.add(titleLabel);
        boolean found = false;
        for(Production p : productions) {
            for (Rating r : p.ratings) {
                if (r.user.equals(u.username)) {
                    JLabel ratingLabel = new JLabel(p.title + ": " + r.rating + " / 10");
                    ratingLabel.setForeground(Color.WHITE);
                    ratingLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    detailsPanel.add(ratingLabel);
                    found = true;
                    break;
                }
            }
        }
        if(!found){
            JOptionPane.showMessageDialog(null, "No ratings found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String ratingTitle;
        if(detailsPanel.getPreferredSize().height > 500 || detailsPanel.getPreferredSize().width > 700){
            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setPreferredSize(new Dimension(700, 500));
            scrollPane.setBackground(new Color(34, 34, 34));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            ratingTitle=JOptionPane.showInputDialog(null, scrollPane, "Delete rating",
                    JOptionPane.PLAIN_MESSAGE);
        }
        else {
            ratingTitle=JOptionPane.showInputDialog(null, detailsPanel, "Delete rating",
                    JOptionPane.PLAIN_MESSAGE);
        }
        boolean found2 = false;
        for(Production p : productions) {
            if(p.title.equalsIgnoreCase(ratingTitle)){
                for (Rating r : p.ratings) {
                    if (r.user.equals(u.username)) {
                        p.ratings.remove(r);
                        ((Regular) u).removeRating();
                        p.calculateRating();
                        productionsPanel.removeAll();
                        makeProdList(null);
                        productionsPanel.revalidate();
                        productionsPanel.repaint();
                        infoPanel.removeAll();
                        makeInfoPanel();
                        infoPanel.revalidate();
                        infoPanel.repaint();
                        JOptionPane.showMessageDialog(null, "Rating deleted!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        found2 = true;
                        return;
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Rating not found!",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addProductionOption(){
        String[] options = {"Movie", "Series"};
        int option2 = JOptionPane.showOptionDialog(null, "Add production", "Add production",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (option2 == 0) {
            JTextField titleField = new JTextField();
            JTextField subjectField = new JTextField();
            JTextField genresField = new JTextField();
            JTextField actorsField = new JTextField();
            JTextField directorsField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField releaseYearField = new JTextField();
            Object[] fields = {
                    "Title:", titleField,
                    "Subject:", subjectField,
                    "Genres:", genresField,
                    "Actors:", actorsField,
                    "Directors:", directorsField,
                    "Duration:", durationField,
                    "Release year:", releaseYearField
            };
            while(true) {
                int result = JOptionPane.showConfirmDialog(null, fields, "Add movie",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (titleField.getText().isEmpty() || subjectField.getText().isEmpty() ||
                            genresField.getText().isEmpty() || actorsField.getText().isEmpty() ||
                            directorsField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                        break;
                    }
                } else{
                    break;
                }
            }
            boolean valid = true;
            List<Genre> genres = new ArrayList<>();
            for (String s : genresField.getText().split(", ")) {
                try {
                    genres.add(Genre.valueOf(s.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid genre! Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                }
            }
            Long releaseYear = null;
            try {
                if(!releaseYearField.getText().isEmpty()) {
                    releaseYear = Long.parseLong(releaseYearField.getText());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid release year! Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                valid = false;
            }
            if(valid) {
                Movie movie = new Movie(titleField.getText(), Arrays.asList(directorsField.getText().split(", ")),
                        Arrays.asList(actorsField.getText().split(", ")), genres, new ArrayList<>(),
                        subjectField.getText(), 0.0, durationField.getText(), releaseYear);
                ((Staff) u).addProductionSystem(movie);
                productionsPanel.removeAll();
                makeProdList(null);
                productionsPanel.revalidate();
                productionsPanel.repaint();
                JOptionPane.showMessageDialog(null, "Movie added!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                if(u instanceof Contributor){
                    Context context = new Context(new AddProdOrActorXp());
                    u.xp += context.executeStrategy();
                }
                infoPanel.removeAll();
                makeInfoPanel();
                infoPanel.revalidate();
                infoPanel.repaint();
            }
        }
        else{
            JTextField titleField = new JTextField();
            JTextField subjectField = new JTextField();
            JTextField genresField = new JTextField();
            JTextField actorsField = new JTextField();
            JTextField directorsField = new JTextField();
            JTextField releaseYearField = new JTextField();
            JTextField numberOfSeasonsField = new JTextField();
            Object[] fields = {
                    "Title:", titleField,
                    "Subject:", subjectField,
                    "Genres:", genresField,
                    "Actors:", actorsField,
                    "Directors:", directorsField,
                    "Release year:", releaseYearField,
                    "Number of seasons:", numberOfSeasonsField
            };
            while(true) {
                int result = JOptionPane.showConfirmDialog(null, fields, "Add series",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (titleField.getText().isEmpty() || subjectField.getText().isEmpty() ||
                            genresField.getText().isEmpty() || actorsField.getText().isEmpty() ||
                            directorsField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                        break;
                    }
                } else{
                    break;
                }
            }
            boolean valid = true;
            List<Genre> genres = new ArrayList<>();
            for (String s : genresField.getText().split(", ")) {
                try {
                    genres.add(Genre.valueOf(s.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid genre! Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                }
            }
            Long releaseYear = null;
            try {
                if(!releaseYearField.getText().isEmpty()) {
                    releaseYear = Long.parseLong(releaseYearField.getText());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid release year! Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                valid = false;
            }
            Long numberOfSeasons = null;
            try {
                if(!numberOfSeasonsField.getText().isEmpty()) {
                    numberOfSeasons = Long.parseLong(numberOfSeasonsField.getText());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number of seasons! Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                valid = false;
            }
            if(valid) {
                Map<String, List<Episode>> seasonsMap = new HashMap<>();
                if(numberOfSeasons != null && numberOfSeasons > 0) {
                    JOptionPane.showMessageDialog(null, "Please add the seasons and episodes!",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 1; i <= numberOfSeasons; i++) {
                        JTextField seasonField = new JTextField();
                        JTextField episodesField = new JTextField();
                        Object[] seasonFields = {
                                "Season name:", seasonField,
                                "Episodes and duration:", episodesField
                        };
                        while (true) {
                            int result = JOptionPane.showConfirmDialog(null, seasonFields, "Add season",
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (result == JOptionPane.OK_OPTION) {
                                if (seasonField.getText().isEmpty() || episodesField.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            }
                        }
                        List<Episode> episodes = new ArrayList<>();
                        try {
                            for (String s : episodesField.getText().split(", ")) {
                                String[] split = s.split(" - ");
                                episodes.add(new Episode(split[0], split[1]));
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Invalid episodes! Please try again.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            valid = false;
                            break;
                        }
                        seasonsMap.put(seasonField.getText(), episodes);
                    }
                }
                if(valid) {
                    Series series = new Series(titleField.getText(), Arrays.asList(directorsField.getText().split(", ")),
                            Arrays.asList(actorsField.getText().split(", ")), genres, new ArrayList<>(),
                            subjectField.getText(), 0.0, releaseYear, numberOfSeasons, seasonsMap);
                    ((Staff) u).addProductionSystem(series);
                    productionsPanel.removeAll();
                    makeProdList(null);
                    productionsPanel.revalidate();
                    productionsPanel.repaint();
                    JOptionPane.showMessageDialog(null, "Series added!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (u instanceof Contributor) {
                        Context context = new Context(new AddProdOrActorXp());
                        u.xp += context.executeStrategy();
                    }
                    infoPanel.removeAll();
                    makeInfoPanel();
                    infoPanel.revalidate();
                    infoPanel.repaint();
                }
            }
        }
    }

    private void deleteProductionOption() {
        StringBuilder productionsTitles = new StringBuilder();
        for(Object o : ((Staff) u).contributions){
            if(o instanceof Production p) {
                productionsTitles.append(p.title).append(" , ");
            }
        }
        if(!productionsTitles.isEmpty()) {
            String[] prodOptions = productionsTitles.toString().split(" , ");
            String option = (String) JOptionPane.showInputDialog(null, "Choose production",
                    "Delete production", JOptionPane.PLAIN_MESSAGE, null, prodOptions, prodOptions[0]);
            ((Staff) u).removeProductionSystem(option);
            productionsPanel.removeAll();
            makeProdList(null);
            productionsPanel.revalidate();
            productionsPanel.repaint();
            JOptionPane.showMessageDialog(null, "Production deleted!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "You have no productions to delete!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addActorOption(){
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField projectsField = new JTextField();
        Object[] fields = {
                "Name:", nameField,
                "Description:", descriptionField,
                "Projects:", projectsField
        };
        while(true) {
            int result = JOptionPane.showConfirmDialog(null, fields, "Add actor",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                        projectsField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else{
                    break;
                }
            } else{
                break;
            }
        }
        boolean valid = true;
        if (!nameField.getText().matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null, "Invalid name! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        Map<String,String> projects = new HashMap<>();
        try {
            for (String s : projectsField.getText().split(", ")) {
                String[] split = s.split(" - ");
                projects.put(split[0], split[1]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid projects! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if(valid) {
            Actor actor = new Actor(nameField.getText(), projects, descriptionField.getText());
            ((Staff) u).addActorSystem(actor);
            actorsPanel.removeAll();
            makeActorsPanel();
            actorsPanel.revalidate();
            actorsPanel.repaint();
            JOptionPane.showMessageDialog(null, "Actor added!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            if(u instanceof Contributor){
                Context context = new Context(new AddProdOrActorXp());
                u.xp += context.executeStrategy();
            }
            infoPanel.removeAll();
            makeInfoPanel();
            infoPanel.revalidate();
            infoPanel.repaint();
        }
    }

    private void addUserOption(){
        JTextField emailField = new JTextField();
        JTextField nameField = new JTextField();
        Object[] fields = {
                "Email:", emailField,
                "Name:", nameField
        };
        while(true) {
            int result = JOptionPane.showConfirmDialog(null, fields, "Add user",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (emailField.getText().isEmpty() || nameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else{
                    break;
                }
            } else{
                break;
            }
        }
        boolean valid = true;
        if (!emailField.getText().contains("@") || emailField.getText().contains(" ")){
            JOptionPane.showMessageDialog(null, "Invalid email! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (!nameField.getText().matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null, "Invalid name! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if(valid) {
            String[] options = {"Regular", "Contributor", "Admin"};
            int option = JOptionPane.showOptionDialog(null, "Choose user type", "Choose user type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (option == 0) {
                ((Admin) u).addUser(emailField.getText(), nameField.getText(),AccountType.valueOf(options[0].toUpperCase()));
                JOptionPane.showMessageDialog(null, "User added!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (option == 1) {
                ((Admin) u).addUser(emailField.getText(), nameField.getText(),AccountType.valueOf(options[1].toUpperCase()));
                JOptionPane.showMessageDialog(null, "User added!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                ((Admin) u).addUser(emailField.getText(), nameField.getText(),AccountType.valueOf(options[2].toUpperCase()));
                JOptionPane.showMessageDialog(null, "User added!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void deleteUserOption(){
        StringBuilder usersNames = new StringBuilder();
        for(User user : users){
            usersNames.append(user.username).append(", ");
        }
        if(!usersNames.isEmpty()) {
            String[] userOptions = usersNames.toString().split(", ");
            String option = (String) JOptionPane.showInputDialog(null, "Choose user",
                    "Delete user", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);
            ((Admin) u).removeUser(option);
            JOptionPane.showMessageDialog(null, "User deleted!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "You have no users to delete!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteActorOption(){
        StringBuilder actorsNames = new StringBuilder();
        for(Object o : ((Staff) u).contributions){
            if(o instanceof Actor a) {
                actorsNames.append(a.name).append(", ");
            }
        }
        if(!actorsNames.isEmpty()) {
            String[] actorOptions = actorsNames.toString().split(", ");
            String option = (String) JOptionPane.showInputDialog(null, "Choose actor",
                    "Delete actor", JOptionPane.PLAIN_MESSAGE, null, actorOptions, actorOptions[0]);
            ((Staff) u).removeActorSystem(option);
            actorsPanel.removeAll();
            makeActorsPanel();
            actorsPanel.revalidate();
            actorsPanel.repaint();
            JOptionPane.showMessageDialog(null, "Actor deleted!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "You have no actors to delete!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createRequestOption(){
        String[] options = {"DELETE ACCOUNT", "ACTOR ISSUE", "MOVIE ISSUE", "OTHERS"};
        int option = JOptionPane.showOptionDialog(null, "Choose request type", "Choose request type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(option == -1){
            return;
        }
        if (option == 0) {
            JTextField descriptionField = new JTextField();
            Object[] fields = {
                    "Description:", descriptionField
            };
            while(true) {
                int result = JOptionPane.showConfirmDialog(null, fields, "Create request",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (descriptionField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                        break;
                    }
                } else{
                    break;
                }
            }
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            date = LocalDateTime.parse(date.format(formatter), formatter);
            Request request = new Request(RequestType.DELETE_ACCOUNT, date,null,descriptionField.getText(),
                    u.username,"ADMIN");
            if(u instanceof Regular regular){
                regular.createRequest(request);
            }
            else{
                ((Contributor) u).createRequest(request);
            }
            JOptionPane.showMessageDialog(null, "Request created!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            IMDB.getInstance().notificationRequestStaff(request.userResolve);
        } else if (option == 1) {
            JTextField descriptionField = new JTextField();
            Object[] fields = {
                    "Description:", descriptionField
            };
            while(true) {
                int result = JOptionPane.showConfirmDialog(null, fields, "Create request",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (descriptionField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                        String actorName = null;
                        for (Actor a : actors) {
                            if (descriptionField.getText().contains(a.name)) {
                                actorName = a.name;
                                break;
                            }
                        }
                        if (actorName == null) {
                            JOptionPane.showMessageDialog(null, "Actor name not found!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (u instanceof Contributor contributor) {
                                for (Object o : contributor.contributions) {
                                    if (o instanceof Actor a) {
                                        if (a.name.equals(actorName)) {
                                            JOptionPane.showMessageDialog(null,
                                                    "You cannot create a request for your own actor!",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                    }
                                }
                            }
                            String userResolve = null;
                            for (User user : users) {
                                if (user instanceof Contributor contributor) {
                                    for (Object o : contributor.contributions) {
                                        if (o instanceof Actor a) {
                                            if (a.name.equals(actorName)) {
                                                userResolve = user.username;
                                                break;
                                            }
                                        }
                                    }
                                } else if (user instanceof Admin admin) {
                                    for (Object o : admin.contributions) {
                                        if (o instanceof Actor a) {
                                            if (a.name.equals(actorName)) {
                                                userResolve = user.username;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            LocalDateTime date = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            date = LocalDateTime.parse(date.format(formatter), formatter);
                            Request request = new Request(RequestType.ACTOR_ISSUE, date, actorName,
                                    descriptionField.getText(), u.username, userResolve);
                            if (u instanceof Regular regular) {
                                regular.createRequest(request);
                            } else {
                                ((Contributor) u).createRequest(request);
                            }
                            JOptionPane.showMessageDialog(null, "Request created!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            IMDB.getInstance().notificationRequestStaff(request.userResolve);
                            return;
                        }
                    }
                } else{
                    break;
                }
            }
        } else if (option == 2) {
            JTextField descriptionField = new JTextField();
            Object[] fields = {
                    "Description:", descriptionField
            };
            while(true) {
                int result = JOptionPane.showConfirmDialog(null, fields, "Create request",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (descriptionField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                        String movieTitle = null;
                        for (Production p : productions) {
                            if (descriptionField.getText().contains(p.title)) {
                                movieTitle = p.title;
                                break;
                            }
                        }
                        if (movieTitle == null) {
                            JOptionPane.showMessageDialog(null, "Movie title not found!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (u instanceof Contributor contributor) {
                                for (Object o : contributor.contributions) {
                                    if (o instanceof Production p) {
                                        if (p.title.equals(movieTitle)) {
                                            JOptionPane.showMessageDialog(null,
                                                    "You cannot create a request for your own movie!",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                    }
                                }
                            }
                            String userResolve = null;
                            for (User user : users) {
                                if (user instanceof Contributor contributor) {
                                    for (Object o : contributor.contributions) {
                                        if (o instanceof Production p) {
                                            if (p.title.equals(movieTitle)) {
                                                userResolve = user.username;
                                                break;
                                            }
                                        }
                                    }
                                } else if (user instanceof Admin admin) {
                                    for (Object o : admin.contributions) {
                                        if (o instanceof Production p) {
                                            if (p.title.equals(movieTitle)) {
                                                userResolve = user.username;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            LocalDateTime date = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            date = LocalDateTime.parse(date.format(formatter), formatter);
                            Request request = new Request(RequestType.MOVIE_ISSUE, date, movieTitle,
                                    descriptionField.getText(), u.username, userResolve);
                            if (u instanceof Regular regular) {
                                regular.createRequest(request);
                            } else {
                                ((Contributor) u).createRequest(request);
                            }
                            JOptionPane.showMessageDialog(null, "Request created!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            IMDB.getInstance().notificationRequestStaff(request.userResolve);
                            return;
                        }
                    }
                } else{
                    break;
                }
            }
        } else {
            JTextField descriptionField = new JTextField();
            Object[] fields = {
                    "Description:", descriptionField
            };
            while(true) {
                int result = JOptionPane.showConfirmDialog(null, fields, "Create request",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (descriptionField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                        break;
                    }
                } else{
                    break;
                }
            }
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            date = LocalDateTime.parse(date.format(formatter), formatter);
            Request request = new Request(RequestType.DELETE_ACCOUNT, date,null,descriptionField.getText(),
                    u.username,"ADMIN");
            if(u instanceof Regular regular){
                regular.createRequest(request);
            }
            else{
                ((Contributor) u).createRequest(request);
            }
            JOptionPane.showMessageDialog(null, "Request created!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            IMDB.getInstance().notificationRequestStaff(request.userResolve);
        }
    }

    private void deleteRequestOption(){
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(34, 34, 34));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Your requests:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsPanel.add(titleLabel);
        boolean exists = false;
        for(Request r : requests) {
             if (r.userRequest.equals(u.username)) {
                 JLabel requestLabel;
                 if (r.titleOrActor == null) {
                     requestLabel = new JLabel("    " + r.getType() + ": " + r.getDate() + " | " + r.description);
                 } else {
                     requestLabel = new JLabel("    " + r.titleOrActor + " - " + r.getType() + ": " + r.getDate() + " | " + r.description);
                 }
                 requestLabel.setForeground(Color.WHITE);
                 requestLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                 detailsPanel.add(requestLabel);
                 exists = true;
             }
        }
        if(!exists) {
            JOptionPane.showMessageDialog(null, "You have no requests to delete!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String requestDate;
        if(detailsPanel.getPreferredSize().height > 500 || detailsPanel.getPreferredSize().width > 700){
            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setPreferredSize(new Dimension(700, 500));
            scrollPane.setBackground(new Color(34, 34, 34));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            requestDate=JOptionPane.showInputDialog(null, scrollPane, "Requests",
                    JOptionPane.PLAIN_MESSAGE);
        }
        else {
            requestDate=JOptionPane.showInputDialog(null, detailsPanel, "Requests",
                    JOptionPane.PLAIN_MESSAGE);
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime createdDate = LocalDateTime.parse(requestDate, formatter);
            for(Request r : requests) {
                if (r.getDate().equals(createdDate)) {
                    if (r.userRequest.equals(u.username)) {
                        if(u instanceof Contributor c){
                            c.removeRequest(r);
                        } else{
                            ((Regular) u).removeRequest(r);
                        }
                        JOptionPane.showMessageDialog(null, "Request deleted!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Invalid date!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solveRequestsOption(){
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(34, 34, 34));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Requests to solve");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsPanel.add(titleLabel);
        if(u instanceof Admin a){
            boolean exists = false;
            for(Request r : Admin.RequestHolder.requests){
                JLabel requestLabel;
                if(r.titleOrActor == null){
                    requestLabel = new JLabel("    " + r.getType() + ": " +r.getDate() + " | " +r.description);
                } else{
                    requestLabel = new JLabel("    " + r.titleOrActor + " - " + r.getType() + ": " +r.getDate() + " | " +r.description);
                }
                requestLabel.setForeground(Color.WHITE);
                requestLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                detailsPanel.add(requestLabel);
                exists = true;
            }
            for(Request r : a.requests){
                JLabel requestLabel;
                if(r.titleOrActor == null){
                    requestLabel = new JLabel("    " + r.getType() + ": " +r.getDate() + " | " +r.description);
                } else{
                    requestLabel = new JLabel("    " + r.titleOrActor + " - " + r.getType() + ": " +r.getDate() + " | " +r.description);
                }
                requestLabel.setForeground(Color.WHITE);
                requestLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                detailsPanel.add(requestLabel);
                exists = true;
            }
            if(!exists) {
                JOptionPane.showMessageDialog(null, "No requests to solve!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else{
            boolean exists = false;
            for(Request r : ((Contributor) u).requests){
                JLabel requestLabel = new JLabel("    " + r.getType() + ": " +r.getDate() + " | " +r.description);
                requestLabel.setForeground(Color.WHITE);
                requestLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                detailsPanel.add(requestLabel);
                exists = true;
            }
            if(!exists) {
                JOptionPane.showMessageDialog(null, "No requests to solve!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String requestDate;
        if(detailsPanel.getPreferredSize().height > 500 || detailsPanel.getPreferredSize().width > 700){
            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setPreferredSize(new Dimension(700, 500));
            scrollPane.setBackground(new Color(34, 34, 34));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            requestDate=JOptionPane.showInputDialog(null, scrollPane, "Requests",
                    JOptionPane.PLAIN_MESSAGE);
        }
        else {
            requestDate=JOptionPane.showInputDialog(null, detailsPanel, "Requests",
                    JOptionPane.PLAIN_MESSAGE);
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime createdDate = LocalDateTime.parse(requestDate, formatter);
            boolean found = false;
            for(Request r : Admin.RequestHolder.requests) {
                if(r.getDate().equals(createdDate)){
                    String[] options = {"Solved", "Rejected"};
                    int option = JOptionPane.showOptionDialog(null, "Mark the request as:", "Solve request",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    if(option == -1){
                        return;
                    }
                    if(option == 0) {
                        JOptionPane.showMessageDialog(null, "Request solved!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        IMDB.getInstance().notificationRequestStatus(r.userRequest,true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Request rejected!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        IMDB.getInstance().notificationRequestStatus(r.userRequest,false);
                    }
                    for (User user : users) {
                        if (user.username.equals(r.userRequest)) {
                            if(user instanceof Contributor c){
                                c.removeRequest(r);
                            } else{
                                ((Regular) user).removeRequest(r);
                            }
                            break;
                        }
                    }
                    found = true;
                    break;
                }
            }
            if(!found){
                for(Request r : ((Staff) u).requests) {
                    if(r.getDate().equals(createdDate)){
                        String[] options = {"Solved", "Rejected"};
                        int option = JOptionPane.showOptionDialog(null, "Mark the request as:", "Solve request",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                        if(option == -1){
                            return;
                        }
                        if(option == 0) {
                            JOptionPane.showMessageDialog(null, "Request solved!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            IMDB.getInstance().notificationRequestStatus(r.userRequest,true);
                            for (User user : users) {
                                if (user.username.equals(r.userRequest)) {
                                    Context context = new Context(new AddRequestXp());
                                    user.xp += context.executeStrategy();
                                    if(user instanceof Contributor c){
                                        c.removeRequest(r);
                                    } else{
                                        ((Regular) user).removeRequest(r);
                                    }
                                    break;
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Request rejected!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            IMDB.getInstance().notificationRequestStatus(r.userRequest,false);
                            for (User user : users) {
                                if (user.username.equals(r.userRequest)) {
                                    if(user instanceof Contributor c){
                                        c.removeRequest(r);
                                    } else{
                                        ((Regular) user).removeRequest(r);
                                    }
                                    break;
                                }
                            }
                        }
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                JOptionPane.showMessageDialog(null, "Request not found! Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Invalid date! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showProdDetails(Production p){
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(34, 34, 34));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel(p.title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsPanel.add(titleLabel);
        JLabel ratingLabel = new JLabel("Rating: " + p.totalRating + " / 10");
        ratingLabel.setForeground(Color.WHITE);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(ratingLabel);
        JLabel genresLabel = new JLabel("Genres: " + p.genres);
        genresLabel.setForeground(Color.WHITE);
        genresLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(genresLabel);
        JLabel descriptionLabel = new JLabel("Description: " + p.subject);
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(descriptionLabel);
        JLabel actorsLabel = new JLabel("Actors: " + p.actors);
        actorsLabel.setForeground(Color.WHITE);
        actorsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(actorsLabel);
        JLabel directorLabel = new JLabel("Director: " + p.directors);
        directorLabel.setForeground(Color.WHITE);
        directorLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(directorLabel);
        if(p instanceof Movie m){
            JLabel durationLabel = new JLabel("Duration: " + m.duration);
            durationLabel.setForeground(Color.WHITE);
            durationLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            detailsPanel.add(durationLabel);
            JLabel releaseYearLabel = new JLabel("Release year: " + m.releaseYear);
            releaseYearLabel.setForeground(Color.WHITE);
            releaseYearLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            detailsPanel.add(releaseYearLabel);
        }
        else{
            JLabel releaseYearLabel = new JLabel("Release year: " + ((Series) p).releaseYear);
            releaseYearLabel.setForeground(Color.WHITE);
            releaseYearLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            detailsPanel.add(releaseYearLabel);
            JLabel numberOfSeasonsLabel = new JLabel("Number of seasons: " + ((Series) p).numberOfSeasons);
            numberOfSeasonsLabel.setForeground(Color.WHITE);
            numberOfSeasonsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            detailsPanel.add(numberOfSeasonsLabel);
            JLabel seasonsLabel = new JLabel("Seasons and episodes:");
            seasonsLabel.setForeground(Color.WHITE);
            seasonsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            detailsPanel.add(seasonsLabel);
            for (Map.Entry<String, List<Episode>> entry : ((Series) p).getSeasonsMap().entrySet()) {
                JLabel seasonLabel = new JLabel("   " + entry.getKey() + ":");
                seasonLabel.setForeground(Color.WHITE);
                seasonLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                detailsPanel.add(seasonLabel);
                List<Episode> episodes = entry.getValue();
                for (Episode e : episodes) {
                    JLabel episodeLabel = new JLabel("      " + e.name + " " + e.duration);
                    episodeLabel.setForeground(Color.WHITE);
                    episodeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                    detailsPanel.add(episodeLabel);
                }
            }
        }
        JLabel ratingListLabel = new JLabel("User reviews:");
        ratingListLabel.setForeground(Color.WHITE);
        ratingListLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(ratingListLabel);
        Comparator<Rating> compareXp = Comparator.comparingInt(rating -> findUserXp(rating.user));
        if(!p.ratings.isEmpty()) {
            p.ratings.sort(compareXp.reversed());
            for (Rating r : p.ratings) {
                JLabel ratingText = new JLabel("    " + r.user + " - " + r.rating + " - " + r.comments);
                ratingText.setForeground(Color.WHITE);
                ratingText.setFont(new Font("Arial", Font.PLAIN, 15));
                detailsPanel.add(ratingText);
            }
        } else{
            JLabel noRatingsLabel = new JLabel("    No ratings yet");
            noRatingsLabel.setForeground(Color.WHITE);
            noRatingsLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            detailsPanel.add(noRatingsLabel);
        }

        if(detailsPanel.getPreferredSize().height > 500 || detailsPanel.getPreferredSize().width > 700){
            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setPreferredSize(new Dimension(700, 500));
            scrollPane.setBackground(new Color(34, 34, 34));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            JOptionPane.showMessageDialog(null, scrollPane, "Production Details",
                    JOptionPane.PLAIN_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, detailsPanel, "Production Details",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void showActorDetails(Actor a){
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(34, 34, 34));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = new JLabel(a.name);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsPanel.add(nameLabel);
        JLabel descriptionLabel = new JLabel("Biography: " + a.biography);
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(descriptionLabel);
        JLabel moviesLabel = new JLabel("Projects: " + a.projects);
        moviesLabel.setForeground(Color.WHITE);
        moviesLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        detailsPanel.add(moviesLabel);

        if(detailsPanel.getPreferredSize().width > 700){
            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setPreferredSize(new Dimension(700, 150));
            scrollPane.setBackground(new Color(34, 34, 34));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            JOptionPane.showMessageDialog(null, scrollPane, "Actor Details",
                    JOptionPane.PLAIN_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, detailsPanel, "Actor Details",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private int findUserXp(String username){
        for (User user : IMDB.getInstance().users) {
            if (user.username.equals(username)) {
                return user.xp;
            }
        }
        return 0;
    }
    public void run() {
        new GUI().setVisible(true);
    }

}
