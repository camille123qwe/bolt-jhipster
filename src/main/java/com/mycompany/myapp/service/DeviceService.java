package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.repository.DeviceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Device}.
 */
@Service
@Transactional
public class DeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Save a device.
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    public Device save(Device device) {
        LOG.debug("Request to save Device : {}", device);
        return deviceRepository.save(device);
    }

    /**
     * Update a device.
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    public Device update(Device device) {
        LOG.debug("Request to update Device : {}", device);
        return deviceRepository.save(device);
    }

    /**
     * Partially update a device.
     *
     * @param device the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Device> partialUpdate(Device device) {
        LOG.debug("Request to partially update Device : {}", device);

        return deviceRepository
            .findById(device.getId())
            .map(existingDevice -> {
                if (device.getUuid() != null) {
                    existingDevice.setUuid(device.getUuid());
                }
                if (device.getName() != null) {
                    existingDevice.setName(device.getName());
                }
                if (device.getLabel() != null) {
                    existingDevice.setLabel(device.getLabel());
                }
                if (device.getDescription() != null) {
                    existingDevice.setDescription(device.getDescription());
                }
                if (device.getRole() != null) {
                    existingDevice.setRole(device.getRole());
                }
                if (device.getCreatedAt() != null) {
                    existingDevice.setCreatedAt(device.getCreatedAt());
                }

                return existingDevice;
            })
            .map(deviceRepository::save);
    }

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        LOG.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }

    /**
     * Get one device by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        LOG.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id);
    }

    /**
     * Delete the device by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
