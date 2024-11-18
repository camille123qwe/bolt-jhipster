import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './source.reducer';

export const SourceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sourceEntity = useAppSelector(state => state.app1.source.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sourceDetailsHeading">
          <Translate contentKey="app1App.app1Source.detail.title">Source</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="app1App.app1Source.id">Id</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="app1App.app1Source.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="app1App.app1Source.name">Name</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.name}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="app1App.app1Source.label">Label</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="app1App.app1Source.description">Description</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="app1App.app1Source.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.createdAt ? <TextFormat value={sourceEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/app1/source" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app1/source/${sourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SourceDetail;
