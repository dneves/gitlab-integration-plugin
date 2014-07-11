package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.ComboBox;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.gitlab.api.models.GitlabUser;

public class GLIssuesFilterView extends JPanel implements UsersHolder {

    private final JTextField textFilter = new JTextField();

    private final JCheckBox checkClosed = new JCheckBox( "Show closed issues", false );

    private final JLabel labelAuthor = new JLabel( "Author" );
    private final ComboBox author = new ComboBox();

    private final JLabel labelAssignee = new JLabel( "Assignee" );
    private final ComboBox assignee = new ComboBox();


    private final GLIController controller;

    private final GLIssueListView listView;

    public GLIssuesFilterView(final GLIController controller, final GLIssueListView listView) {
        this.controller = controller;
        this.listView = listView;

        setupComponents();
        setupLayout();

        ProgressManager.getInstance().run( new GetUsersTask( controller, this ) );
    }

    private void setupComponents() {
        author.setPreferredSize( new Dimension( 200, 18 ) );
        assignee.setPreferredSize(new Dimension( 200, 18 ) );
        textFilter.setPreferredSize( new Dimension( 225, 18 ) );

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listView.filter((String) author.getSelectedItem(), (String) assignee.getSelectedItem(), textFilter.getText(), checkClosed.isSelected());
            }
        };

        checkClosed.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                listView.filter( ( String ) author.getSelectedItem(), ( String ) assignee.getSelectedItem(), textFilter.getText(), checkClosed.isSelected() );
            }
        });
        textFilter.addActionListener( actionListener );
        author.addActionListener(actionListener);
        assignee.addActionListener(actionListener);
    }

    private void setupLayout() {
        TableLayout layout = new TableLayout(
                new double[] {
                        TableLayout.MINIMUM, TableLayout.PREFERRED,
                        TableLayout.MINIMUM, TableLayout.PREFERRED,
                        TableLayout.MINIMUM, TableLayout.PREFERRED,
                        TableLayout.PREFERRED
                },
                new double[] { TableLayout.PREFERRED }
        );
        layout.setHGap( 8 );
        this.setLayout( layout );

        this.add( labelAuthor, new TableLayoutConstraints( 0, 0 ) );
        this.add( author, new TableLayoutConstraints( 1, 0 ) );
        this.add( labelAssignee, new TableLayoutConstraints( 2, 0 ) );
        this.add( assignee, new TableLayoutConstraints( 3, 0 ) );
        this.add( new JLabel( "Filter" ), new TableLayoutConstraints( 4, 0 ) );
        this.add( textFilter, new TableLayoutConstraints( 5, 0 ) );
        this.add( checkClosed, new TableLayoutConstraints( 6, 0 ) );
    }

    @Override
    public void setUsers( Collection<GitlabUser> users) {
        author.removeAllItems();
        assignee.removeAllItems();

        author.addItem( "" );
        assignee.addItem( "" );

        if ( users != null ) {
            for (GitlabUser user : users) {
                if ( "active".equalsIgnoreCase( user.getState() ) ) {
                    String userLabel = GLIController.getLabel(user);
                    author.addItem(userLabel);
                    assignee.addItem(userLabel);
                }
            }
        }
    }

}
