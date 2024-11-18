import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './execution-type.reducer';

export const ExecutionTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const executionTypeEntity = useAppSelector(state => state.app1.executionType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="executionTypeDetailsHeading">
          <Translate contentKey="app1App.app1ExecutionType.detail.title">ExecutionType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="app1App.app1ExecutionType.id">Id</Translate>
            </span>
          </dt>
          <dd>{executionTypeEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="app1App.app1ExecutionType.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{executionTypeEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="app1App.app1ExecutionType.name">Name</Translate>
            </span>
          </dt>
          <dd>{executionTypeEntity.name}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="app1App.app1ExecutionType.label">Label</Translate>
            </span>
          </dt>
          <dd>{executionTypeEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="app1App.app1ExecutionType.description">Description</Translate>
            </span>
          </dt>
          <dd>{executionTypeEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="app1App.app1ExecutionType.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {executionTypeEntity.createdAt ? (
              <TextFormat value={executionTypeEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/app1/execution-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app1/execution-type/${executionTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExecutionTypeDetail;
