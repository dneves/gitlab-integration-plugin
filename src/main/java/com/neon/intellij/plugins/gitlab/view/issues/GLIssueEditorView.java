//package com.neon.intellij.plugins.gitlab.view.issues;
//
//import com.intellij.icons.AllIcons;
//import com.intellij.openapi.diagnostic.Logger;
//import com.neon.intellij.plugins.gitlab.controller.editor.GLIssueVirtualFile;
//import com.neon.intellij.plugins.gitlab.model.EditableView;
//import info.clearthought.layout.TableLayout;
//import info.clearthought.layout.TableLayoutConstraints;
//import org.gitlab.api.models.GitlabIssue;
//import org.jetbrains.annotations.NotNull;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//
//public class GLIssueEditorView extends JPanel implements EditableView<GitlabIssue, GitlabIssue> {
//
//    private static final Logger LOG = Logger.getInstance("gitlab");
//
//    private final JButton buttonSave = new JButton( "save", AllIcons.Actions.Menu_saveall );
//    private final JButton buttonClose = new JButton( "close", AllIcons.Actions.Close );
//
//    private final JLabel labelTitle = new JLabel( "Title" );
//    private final JTextField textTitle = new JTextField();
//
//    private final JLabel labelDescription = new JLabel( "Description" );
//    private final JTextArea textDescription = new JTextArea( 5, 20 );
//
//    private GLIssueVirtualFile virtualFile;
//
//    private GitlabIssue model;
//
//
//    public GLIssueEditorView( @NotNull final GLIssueVirtualFile vf ) {
//        this.virtualFile = vf;
//        this.model = vf.getIssue();
//
//        setupComponents();
//        setupLayout();
//
//        fill( vf.getIssue() );
//    }
//
//    private void setupComponents() {
//        textDescription.setWrapStyleWord( true );
//        textDescription.setLineWrap( true );
//
//        buttonSave.addActionListener( new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                virtualFile.setIssue( save() );
//                try {
//                    virtualFile.saveAndClose();
//                } catch (IOException e1) {
//                    LOG.error( e1 );
//                }
//            }
//        });
//        buttonClose.addActionListener( new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                virtualFile.close();
//            }
//        });
//    }
//
//    private void setupLayout() {
//        JScrollPane dp = new JScrollPane( textDescription, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
//
//        TableLayout layoutFields = new TableLayout(
//                new double[]{TableLayout.MINIMUM, TableLayout.FILL},
//                new double[]{TableLayout.MINIMUM, TableLayout.FILL}
//        );
//        layoutFields.setHGap( 5 );
//        layoutFields.setVGap( 5 );
//        JPanel fieldsPanel = new JPanel( layoutFields );
//        fieldsPanel.add( labelTitle, new TableLayoutConstraints( 0, 0, 0, 0 ) );
//        fieldsPanel.add( textTitle, new TableLayoutConstraints( 1, 0, 1, 0 ) );
//
//        fieldsPanel.add( labelDescription, new TableLayoutConstraints( 0, 1, 0, 1, TableLayout.LEFT, TableLayout.TOP ) );
//        fieldsPanel.add( dp, new TableLayoutConstraints( 1, 1, 1, 1 ) );
//
//        TableLayout layoutButtons = new TableLayout(
//                new double[]{TableLayout.MINIMUM, TableLayout.MINIMUM},
//                new double[]{TableLayout.MINIMUM}
//        );
//        layoutButtons.setHGap( 5 );
//        JPanel panelBottom = new JPanel( layoutButtons );
//        panelBottom.add( buttonSave, new TableLayoutConstraints( 0, 0, 0, 0 ) );
//        panelBottom.add( buttonClose, new TableLayoutConstraints( 1, 0, 1, 0 ) );
//
//
//        TableLayout layout = new TableLayout(
//                new double[]{TableLayout.FILL},
//                new double[]{TableLayout.FILL, TableLayout.MINIMUM}
//        );
//        layout.setHGap( 5 );
//        layout.setVGap( 5 );
//        this.setLayout( layout );
//        this.add( fieldsPanel, new TableLayoutConstraints(0, 0, 0, 0 ) );
//        this.add( panelBottom, new TableLayoutConstraints(0, 1, 0, 1, TableLayout.CENTER, TableLayout.CENTER) );
//    }
//
//    private void clear() {
//        textTitle.setText( "" );
//        textDescription.setText( "" );
//    }
//
//    @Override
//    public void fill( GitlabIssue issue ) {
//        clear();
//        textTitle.setText( issue.getTitle() );
//        textDescription.setText( issue.getDescription() );
//    }
//
//    @Override
//    public GitlabIssue save() {
//        model.setTitle( textTitle.getText() );
//        model.setDescription( textDescription.getText() );
//        return model;
//    }
//
//}
