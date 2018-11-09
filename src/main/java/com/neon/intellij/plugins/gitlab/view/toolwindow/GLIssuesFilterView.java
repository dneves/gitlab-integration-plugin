package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.openapi.ui.ComboBox;
import com.neon.intellij.plugins.gitlab.GIPUserObserver;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class GLIssuesFilterView extends JPanel implements GIPUserObserver {

    private final JTextField textFilter = new JTextField();

    private final JCheckBox checkClosed = new JCheckBox( "Show closed issues", false );
    private final JCheckBox checkEmpty = new JCheckBox( "Show empty nodes", false );

    private final JLabel labelAuthor = new JLabel( "Author" );
    private final ComboBox< GIPUser > author = new ComboBox<>();

    private final JLabel labelAssignee = new JLabel( "Assignee" );
    private final ComboBox< GIPUser > assignee = new ComboBox<>();


    private final GLIssueListView listView;

    public GLIssuesFilterView(final GLIssueListView listView) {
        this.listView = listView;

        setupComponents();
        setupLayout();
    }

    private void setupComponents() {
        author.setPreferredSize( new Dimension( 200, 18 ) );
        assignee.setPreferredSize(new Dimension( 200, 18 ) );
        textFilter.setPreferredSize( new Dimension( 225, 18 ) );

        checkClosed.addItemListener(e -> filter());
        checkEmpty.addItemListener(e -> filter());

        ActionListener actionListener = e -> filter();
        textFilter.addActionListener( actionListener );
        author.addActionListener(actionListener);
        assignee.addActionListener(actionListener);

        author.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel( value == null ? " " : value.name != null && !value.name.trim().isEmpty() ? value.name :
                        value.username != null && ! value.username.trim().isEmpty() ? value.username : " " ));
        assignee.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel( value == null ? " " : value.name != null && !value.name.trim().isEmpty() ? value.name :
                        value.username != null && ! value.username.trim().isEmpty() ? value.username : " " ));

    }

    private void setupLayout() {
        TableLayout layout = new TableLayout(
                new double[] {
                        TableLayout.MINIMUM, TableLayout.PREFERRED,
                        TableLayout.MINIMUM, TableLayout.PREFERRED,
                        TableLayout.MINIMUM
                },
                new double[] { TableLayout.FILL, TableLayout.FILL }
        );
        layout.setHGap( 5 );
        layout.setVGap( 5 );
        this.setLayout( layout );

        this.add( labelAuthor, new TableLayoutConstraints( 0, 0 ) );
        this.add( author, new TableLayoutConstraints( 1, 0 ) );
        this.add( labelAssignee, new TableLayoutConstraints( 2, 0 ) );
        this.add( assignee, new TableLayoutConstraints( 3, 0 ) );

        this.add( checkClosed, new TableLayoutConstraints( 4, 0 ) );

        this.add( new JLabel( "Filter" ), new TableLayoutConstraints( 0, 1 ) );
        this.add( textFilter, new TableLayoutConstraints( 1, 1, 3, 1 ) );

        this.add( checkEmpty, new TableLayoutConstraints( 4, 1 ) );
    }


    @Override
    public void onStart() {
        author.removeAllItems();
        assignee.removeAllItems();

        author.addItem( new GIPUser() );
        assignee.addItem( new GIPUser() );
    }

    @Override
    public void accept(List<GIPUser> users) {
        if ( users != null ) {
            users.forEach( user -> {
                author.addItem( user );
                assignee.addItem( user );
            } );
        }
    }

    private void filter() {
        GIPUser author = this.author.getModel().getElementAt(this.author.getSelectedIndex());
        GIPUser assignee = this.assignee.getModel().getElementAt(this.assignee.getSelectedIndex());

        listView.filter(
                author,
                assignee,
                textFilter.getText(),
                checkClosed.isSelected(),
                checkEmpty.isSelected() );
    }

}
