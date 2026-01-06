package org.example.server.Service;

import jakarta.transaction.Transactional;
import org.example.server.Model.Layout;
import org.example.server.Model.Repository.LayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayoutService {
    @Autowired
    private LayoutRepository layoutRepository;

    public Layout addLayout(Layout layout) {
        return layoutRepository.save(layout);
    }

    public Page<Layout> getAllLayoutsByModId(int modId, Pageable pageable) {
        return layoutRepository.findByModId(modId, pageable);
    }

    public Page<Layout> getAllLayoutsByModId2(int modId, Pageable pageable) {
        return layoutRepository.findByModIdStatus(modId, pageable);
    }
    @Transactional
    public void deleteLayout(int layoutId) {
        layoutRepository.deleteByLayoutId(layoutId);
    }

    public Layout getLayoutByLayoutId(int layoutId){
        return layoutRepository.findByLayoutId(layoutId);
    }
    @Transactional
    public void updateStatusByLayoutId(int layoutId, int status){
        layoutRepository.updateStatusByLayoutId(layoutId, status);
    }

    @Transactional
    public void updateLayout(String nameLayout, int seatCapacity, int x, int y, int status, int layoutId){
        layoutRepository.updateLayout(nameLayout, seatCapacity, x, y, status, layoutId);
    }
}
