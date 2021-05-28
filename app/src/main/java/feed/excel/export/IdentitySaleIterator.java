package feed.excel.export;

import com.depi.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IdentitySaleIterator implements Iterator<Sale> {

    private Long lastId;
    private BiFunction<Long, Pageable, Page<Sale>> getNextBatchFunction;
    private Function<Long, Long> restPartSizeFunction;
    private int batchSize;
    private Sort sort;
    int page;

    public IdentitySaleIterator(BiFunction<Long, Pageable, Page<Sale>> getNextBatchFunction, Function<Long, Long> restPartSizeFunction, Pageable pageable, Long from) {
        this.getNextBatchFunction = getNextBatchFunction;
        this.restPartSizeFunction = restPartSizeFunction;
        this.batchSize = pageable.getPageSize();
        this.page = pageable.getPageNumber();
        this.lastId = from;
        sort = Sort.by(Sort.Direction.ASC, "id");
    }
    @Override
    public boolean hasNext() {
        return restPartSizeFunction.apply(lastId) > 0;
    }

    @Override
    public Sale next() {
        Pageable pageable = PageRequest.of(page, batchSize, sort);
        Page<Sale> firstPage = getNextBatchFunction.apply(lastId, pageable);
        Sale firstSaleOnPage = firstPage.stream().findFirst().orElse(null);
        lastId++;
        return firstSaleOnPage;
    }
}
