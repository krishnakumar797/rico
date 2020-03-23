package com.rico.couchbase.services;

import org.springframework.stereotype.Service;

import com.rico.couchbase.documents.User;

import ro.common.couchbase.CommonCBRepository;

@Service
public interface UserService extends CommonCBRepository<User, String> {

}
