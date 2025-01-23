# jpa-locking


### Optimistic Validation

## cURL
```
curl -X 'GET' \
  'http://localhost:9191/booking/optimistic/2' \
  -H 'accept: */*'
```
## Results

```
Thread-1 is attempting to book the seat optimistically...
Thread-1 fetched seat with version : 0
Thread-2 is attempting to book the seat optimistically...
Thread-2 fetched seat with version : 0
Thread-1 successfully booked the seat ! and version is 1
Thread-2 failed: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.javatechie.entity.Seat#2]
```
### Step-by-Step Breakdown

1. **Thread-1 and Thread-2 Fetch the Same Seat (Version 0)**  
   Both threads start their tasks:  
   - **Thread-1** fetches the `Seat` entity with `version: 0`.  
   - Before Thread-1 completes its booking, **Thread-2** also fetches the same `Seat` entity with `version: 0`.  

   This is expected since optimistic locking allows multiple threads to read the same entity at the same time. At this point:  
   - Both threads hold a copy of the entity with `version: 0`.

2. **Thread-1 Successfully Books the Seat**  
   - **Thread-1** proceeds to update the seat:  
     - It checks the `version` of the entity in the database (`version: 0`) and finds it matches the version it fetched.  
     - The update is successful, and the `version` of the entity in the database is incremented to `1`.  
   - **Thread-1 logs**:  
     ```plaintext
     Thread-1 successfully booked the seat! and version is 1
     ```

3. **Thread-2 Fails Due to Version Mismatch**  
   - After Thread-1 updates the seat, **Thread-2** attempts to update the same seat using its fetched copy (`version: 0`).  
   - Before updating, the database checks if the `version` in Thread-2's copy (`version: 0`) matches the current version in the database (`version: 1`).  
   - The versions do **not** match because Thread-1 has already updated the seat.  
   - As a result, Hibernate throws an **OptimisticLockingFailureException**, indicating that another transaction modified the row since Thread-2 fetched it.  
   - **Thread-2 logs**:  
     ```plaintext
     Thread-2 failed: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.javatechie.entity.Seat#2]
     ```

