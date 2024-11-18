import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getExecutionTypes } from 'app/entities/app1/execution-type/execution-type.reducer';
import { getEntities as getSources } from 'app/entities/app1/source/source.reducer';
import { getEntities as getProjects } from 'app/entities/app1/project/project.reducer';
import { Status } from 'app/shared/model/enumerations/status.model';
import { createEntity, getEntity, reset, updateEntity } from './strategy.reducer';

export const StrategyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const executionTypes = useAppSelector(state => state.app1.executionType.entities);
  const sources = useAppSelector(state => state.app1.source.entities);
  const projects = useAppSelector(state => state.app1.project.entities);
  const strategyEntity = useAppSelector(state => state.app1.strategy.entity);
  const loading = useAppSelector(state => state.app1.strategy.loading);
  const updating = useAppSelector(state => state.app1.strategy.updating);
  const updateSuccess = useAppSelector(state => state.app1.strategy.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate(`/app1/strategy${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getExecutionTypes({}));
    dispatch(getSources({}));
    dispatch(getProjects({}));
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
      ...strategyEntity,
      ...values,
      executionType: executionTypes.find(it => it.id.toString() === values.executionType?.toString()),
      source: sources.find(it => it.id.toString() === values.source?.toString()),
      project: projects.find(it => it.id.toString() === values.project?.toString()),
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
          status: 'Active',
          ...strategyEntity,
          createdAt: convertDateTimeFromServer(strategyEntity.createdAt),
          executionType: strategyEntity?.executionType?.id,
          source: strategyEntity?.source?.id,
          project: strategyEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="app1App.app1Strategy.home.createOrEditLabel" data-cy="StrategyCreateUpdateHeading">
            <Translate contentKey="app1App.app1Strategy.home.createOrEditLabel">Create or edit a Strategy</Translate>
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
                  id="strategy-id"
                  label={translate('app1App.app1Strategy.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('app1App.app1Strategy.uuid')}
                id="strategy-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('app1App.app1Strategy.label')}
                id="strategy-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('app1App.app1Strategy.description')}
                id="strategy-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('app1App.app1Strategy.createdAt')}
                id="strategy-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('app1App.app1Strategy.executionRule')}
                id="strategy-executionRule"
                name="executionRule"
                data-cy="executionRule"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('app1App.app1Strategy.status')}
                id="strategy-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate(`app1App.Status.${status}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="strategy-executionType"
                name="executionType"
                data-cy="executionType"
                label={translate('app1App.app1Strategy.executionType')}
                type="select"
              >
                <option value="" key="0" />
                {executionTypes
                  ? executionTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="strategy-source"
                name="source"
                data-cy="source"
                label={translate('app1App.app1Strategy.source')}
                type="select"
              >
                <option value="" key="0" />
                {sources
                  ? sources.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="strategy-project"
                name="project"
                data-cy="project"
                label={translate('app1App.app1Strategy.project')}
                type="select"
              >
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/app1/strategy" replace color="info">
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

export default StrategyUpdate;
