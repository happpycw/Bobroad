package com.grepp.synapse4.app.model.llm;

import com.grepp.synapse4.app.model.llm.dto.AdminCurationResultDto;
import com.grepp.synapse4.app.model.llm.dto.AdminSearchCurationDto;
import com.grepp.synapse4.app.model.llm.repository.CurationResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurationResultService {

    private final CurationResultRepository repository;

    @Transactional(readOnly = true)
    public List<AdminCurationResultDto> getResultsByCurationId() {
        List<AdminCurationResultDto> results = repository.findResultsByCurationId();
        results.sort(Comparator.comparing(AdminCurationResultDto::getCreatedAt).reversed()
                        .thenComparing(AdminCurationResultDto::getTitle));
        return results;
    }

    @Transactional
    public void toggleActive(Long id) {

        // select 를 해서 update 가능한지
        // 가능 -> update 수행
        // transaction 비용 이슈

        int updated = repository.toggleActiveById(id);
        if (updated == 0) {
            throw new EntityNotFoundException("큐레이션 결과가 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<AdminSearchCurationDto> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return List.of();
        }
        return repository.findByCurationTitleContaining(keyword);
    }
}




