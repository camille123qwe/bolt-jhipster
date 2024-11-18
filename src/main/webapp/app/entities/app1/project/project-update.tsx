import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCustomers } from 'app/entities/app1/customer/customer.reducer';
import { ProjectRole } from 'app/shared/model/enumerations/project-role.model';
import { createEntity, getEntity, reset, updateEntity } from './project.reducer';

export const ProjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customers = useAppSelector(state => state.app1.customer.entities);
  const projectEntity = useAppSelector(state => state.app1.project.entity);
  const loading = useAppSelector(state => state.app1.project.loading);
  const updating = useAppSelector(state => state.app1.project.updating);
  const updateSuccess = useAppSelector(state => state.app1.project.updateSuccess);
  const projectRoleValues = Object.keys(ProjectRole);

  const handleClose = () => {
    navigate(`/app1/project${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCustomers({}));
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
      ...projectEntity,
      ...values,
      customer: customers.find(it => it.id.toString() === values.customer?.toString()),
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
          role: 'STRATEGY_OWNER',
          ...projectEntity,
          createdAt: convertDateTimeFromServer(projectEntity.createdAt),
          customer: projectEntity?.customer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="app1App.app1Project.home.createOrEditLabel" data-cy="ProjectCreateUpdateHeading">
            <Translate contentKey="app1App.app1Project.home.createOrEditLabel">Create or edit a Project</Translate>
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
                  id="project-id"
                  label={translate('app1App.app1Project.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('app1App.app1Project.uuid')}
                id="project-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('app1App.app1Project.name')}
                id="project-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('app1App.app1Project.label')} id="project-label" name="label" data-cy="label" type="text" />
              <ValidatedField
                label={translate('app1App.app1Project.description')}
                id="project-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('app1App.app1Project.role')} id="project-role" name="role" data-cy="role" type="select">
                {projectRoleValues.map(projectRole => (
                  <option value={projectRole} key={projectRole}>
                    {translate(`app1App.ProjectRole.${projectRole}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('app1App.app1Project.createdAt')}
                id="project-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="project-customer"
                name="customer"
                data-cy="customer"
                label={translate('app1App.app1Project.customer')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/app1/project" replace color="info">
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

export default ProjectUpdate;
