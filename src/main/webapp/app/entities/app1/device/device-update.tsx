import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { DeviceRole } from 'app/shared/model/enumerations/device-role.model';
import { createEntity, getEntity, reset, updateEntity } from './device.reducer';

export const DeviceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const deviceEntity = useAppSelector(state => state.app1.device.entity);
  const loading = useAppSelector(state => state.app1.device.loading);
  const updating = useAppSelector(state => state.app1.device.updating);
  const updateSuccess = useAppSelector(state => state.app1.device.updateSuccess);
  const deviceRoleValues = Object.keys(DeviceRole);

  const handleClose = () => {
    navigate(`/app1/device${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...deviceEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          role: 'ACTION_DEVICE',
          ...deviceEntity,
          createdAt: convertDateTimeFromServer(deviceEntity.createdAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="app1App.app1Device.home.createOrEditLabel" data-cy="DeviceCreateUpdateHeading">
            <Translate contentKey="app1App.app1Device.home.createOrEditLabel">Create or edit a Device</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="device-id"
                  label={translate('app1App.app1Device.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('app1App.app1Device.uuid')}
                id="device-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('app1App.app1Device.name')}
                id="device-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('app1App.app1Device.label')} id="device-label" name="label" data-cy="label" type="text" />
              <ValidatedField
                label={translate('app1App.app1Device.description')}
                id="device-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('app1App.app1Device.role')} id="device-role" name="role" data-cy="role" type="select">
                {deviceRoleValues.map(deviceRole => (
                  <option value={deviceRole} key={deviceRole}>
                    {translate(`app1App.DeviceRole.${deviceRole}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('app1App.app1Device.createdAt')}
                id="device-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/app1/device" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DeviceUpdate;
