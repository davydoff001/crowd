/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.crowd.view;

import com.example.crowd.components.EmployeeEditor;
import com.example.crowd.domain.Employee;
import com.example.crowd.repo.EmployeeRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author a.davydov
 */
@Route
public class MainView extends VerticalLayout{
    private final EmployeeRepo employeeRepo;

    private Grid<Employee> grid = new Grid<>(Employee.class);  
    final TextField filter = new TextField("","Type to filter");    
    private final Button addNewBtn = new Button("Add new",VaadinIcon.PLUS.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(filter,addNewBtn);
    
    private final EmployeeEditor editor;
    
    @Autowired
    public MainView(EmployeeRepo employeeRepo, EmployeeEditor editor) {
        this.employeeRepo = employeeRepo;
        this.editor = editor;
          
        filter.setPlaceholder("Type to filter");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showEmployee(e.getValue()));
        
        add(toolbar,grid,editor);
        
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEmployee(e.getValue());
        });
        
        addNewBtn.addClickListener(e -> editor.editEmployee(new Employee()));
        
        editor.setChangeHandler(()-> {
            editor.setVisible(false);
            showEmployee(filter.getValue());
        });
        
        showEmployee("");
    }
    
    private void showEmployee(String name){
        if(name.isEmpty()){
            grid.setItems(employeeRepo.findAll());
        } else {
            grid.setItems(employeeRepo.findByName(name));
        }
        
    }
    
}
