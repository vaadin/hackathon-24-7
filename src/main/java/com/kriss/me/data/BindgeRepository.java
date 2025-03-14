package com.kriss.me.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BindgeRepository extends JpaRepository<Bindge, Long>, JpaSpecificationExecutor<Bindge> {

}
