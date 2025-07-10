package firebase.service

import dev.gitlive.firebase.firestore.FirebaseFirestore

class StorageServiceImpl(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    // Stub implementation for desktop
    // Implement all methods as no-ops or throw NotImplementedError
} 