package org.example.server.Service;

import org.example.server.Model.Layout;
import org.example.server.Model.Repository.LayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LayoutService {
    @Autowired
    private LayoutRepository layoutRepository;

    public void addLayout(Layout layout) {
        layoutRepository.save(layout);
    }

}
